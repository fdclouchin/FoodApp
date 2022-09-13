package com.example.foodapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapters.CartAdapter;
import com.example.foodapp.Helper.SwipeHelper;
import com.example.foodapp.Helper.SwipeHelper2;
import com.example.foodapp.Interfaces.ButtonClickListener;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartInformationFragment extends Fragment implements OnBackPressedFragment, EditCartItemFragment.RefreshCart {
    private static final String CART_ID = "CART_ID";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String ITEM_PRICE = "ITEM_PRICE";
    private static final String ITEM_IMAGE = "ITEM_IMAGE";
    private static final String NO_OF_ITEMS = "NO_OF_ITEMS";

    private final String mActionBarTitle;
    private CartInformationFragment mCartInformationFragment;
    private ImageView mBackButton;
    private TextView mTitle;

    private RecyclerView mCartRecyclerView;
    private CartAdapter mCartAdapter;
    private TextView mCartSize;
    private ProgressDialog mDialog;
    private ArrayList<Cart> mCartList;

    private ConstraintLayout mOrderSummaryLayout;
    private TextView mSubTotal;
    private TextView mVatTaxTotal;
    private TextView mCartTotal;
    private TextView mCheckout;
    private TextView mRemoveAll;

    private ConstraintLayout mEmpty;

    private double totalPrice;
    private double vatTotal;

    private CartDatabase cartDB;

    public CartInformationFragment(String title) {
        this.mActionBarTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_information, container, false);

        mBackButton = view.findViewById(R.id.back_button);
        mTitle = view.findViewById(R.id.text_view_title);
        mCartSize = view.findViewById(R.id.cart_size);

        mOrderSummaryLayout = view.findViewById(R.id.order_summary_layout);
        mSubTotal = view.findViewById(R.id.sub_total_price);
        mVatTaxTotal = view.findViewById(R.id.vat_tax_total);
        mCartTotal = view.findViewById(R.id.cart_total);
        mCheckout = view.findViewById(R.id.check_out_button);
        mRemoveAll = view.findViewById(R.id.remove_all_items);
        mEmpty = view.findViewById(R.id.empty_cart);

        mTitle.setText(mActionBarTitle);

        mCheckout.setOnClickListener(mOnClickListener);
        mBackButton.setOnClickListener(mOnClickListener);
        mRemoveAll.setOnClickListener(mOnClickListener);
        mDialog = new ProgressDialog(getContext());

        cartDB = CartDatabase.getDbInstance(getContext().getApplicationContext());

        mCartInformationFragment = (CartInformationFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.display_fragment);
        initRecyclerView(view);

        //((SimpleItemAnimator) mCartRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        return view;
    }

    private void initRecyclerView(View view) {
        mCartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mCartRecyclerView.addItemDecoration(dividerItemDecoration);

        mCartAdapter = new CartAdapter(getContext());
        mCartRecyclerView.setAdapter(mCartAdapter);
        loadProgressDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCartList();
            }
        }, 1000);

        //swipeHelper();
        swipeHelper2();
    }

    private void swipeHelper2() {
        SwipeHelper2 swipeHelper = new SwipeHelper2(getContext(), mCartRecyclerView, 250) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                int deleteIcon = R.drawable.ic_delete;
                Bitmap bitmapDelete = getBitmapFromVectorDrawable(getContext(), deleteIcon);
                underlayButtons.add(new SwipeHelper2.UnderlayButton(
                        getContext(),
                        45,
                        getString(R.string.delete_label).toUpperCase(),
                        bitmapDelete,
                        ContextCompat.getColor(getContext(), R.color.light_orange),
                        new SwipeHelper2.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Cart data = mCartAdapter.getItem(pos);
                                int cartID = Integer.parseInt(String.valueOf(data.cart_id));
                                showRemoveDialog(cartID);
                            }
                        }
                ));
                int editIcon = R.drawable.ic_edit;
                Bitmap bitmapEdit = getBitmapFromVectorDrawable(getContext(), editIcon);

                underlayButtons.add(new SwipeHelper2.UnderlayButton(
                        getContext(),
                        45,
                        getString(R.string.edit_label).toUpperCase(),
                        bitmapEdit,
                        ContextCompat.getColor(getContext(), R.color.red),
                        new SwipeHelper2.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Cart data = mCartAdapter.getItem(pos);
                                int cartID = Integer.parseInt(String.valueOf(data.cart_id));
                                String cartItemName = data.itemTitle;
                                String cartItemPrice = data.itemPrice;
                                String cartItemImage = data.itemImage;
                                int cartNumberOfItems = data.noOfItems;

                                Bundle updateBundle = new Bundle();
                                updateBundle.putInt(CART_ID, cartID);
                                updateBundle.putString(ITEM_NAME, cartItemName);
                                updateBundle.putString(ITEM_PRICE, cartItemPrice);
                                updateBundle.putString(ITEM_IMAGE, cartItemImage);
                                updateBundle.putInt(NO_OF_ITEMS, cartNumberOfItems);
                                EditCartItemFragment editCartItemFragment = new EditCartItemFragment();
                                editCartItemFragment.setArguments(updateBundle);
                                editCartItemFragment.setRefreshCartCallback(CartInformationFragment.this);
                                displayEditCartItem(editCartItemFragment);
                            }
                        }
                ));
            }
        };
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            assert drawable != null;
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void swipeHelper() {
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), mCartRecyclerView, 230) {
            @Override
            public void instantiateItemButton(RecyclerView.ViewHolder viewHolder, List<ItemButton> buffer) {
                buffer.add(new ItemButton(
                        getContext(),
                        getString(R.string.delete_label).toUpperCase(),
                        0,
                        50,
                        ContextCompat.getColor(getContext(), R.color.light_orange),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Cart data = mCartAdapter.getItem(position);
                                int cartID = Integer.parseInt(String.valueOf(data.cart_id));
                                showRemoveDialog(cartID);
                            }
                        }
                ));
                buffer.add(new ItemButton(
                        getContext(),
                        getString(R.string.edit_label).toUpperCase(),
                        0,
                        50,
                        ContextCompat.getColor(getContext(), R.color.red),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Cart data = mCartAdapter.getItem(position);
                                int cartID = Integer.parseInt(String.valueOf(data.cart_id));
                                String cartItemName = data.itemTitle;
                                String cartItemPrice = data.itemPrice;
                                String cartItemImage = data.itemImage;
                                int cartNumberOfItems = data.noOfItems;

                                Bundle updateBundle = new Bundle();
                                updateBundle.putInt(CART_ID, cartID);
                                updateBundle.putString(ITEM_NAME, cartItemName);
                                updateBundle.putString(ITEM_PRICE, cartItemPrice);
                                updateBundle.putString(ITEM_IMAGE, cartItemImage);
                                updateBundle.putInt(NO_OF_ITEMS, cartNumberOfItems);
                                EditCartItemFragment editCartItemFragment = new EditCartItemFragment();
                                editCartItemFragment.setArguments(updateBundle);
                                editCartItemFragment.setRefreshCartCallback(CartInformationFragment.this);
                                displayEditCartItem(editCartItemFragment);
                            }
                        }
                ));
            }
        };
    }

    private void displayEditCartItem(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.display_fragment, fragment)
                .commit();
    }

    private void loadProgressDialog() {
        mDialog.setTitle(R.string.loading_label);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void loadCartList() {
        mCartList = new ArrayList<>();

        mCartList = (ArrayList<Cart>) cartDB.cartDao().getAllCart();
        if (mCartList != null) {
            mCartAdapter.setCartList(mCartList);
        }

        mCartSize.setText("(" + mCartList.size() + ")");
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }

        getSubTotal();
        getVatTotal();
        getTotal();

        hideOrderSummary();
    }

    private void hideOrderSummary() {
        if (mCartList.isEmpty()) {
            mOrderSummaryLayout.setVisibility(View.GONE);
            mCartRecyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mOrderSummaryLayout.setVisibility(View.VISIBLE);
            mCartRecyclerView.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
        }
    }

    private double getTotal() {
        double cartTotal = totalPrice + vatTotal;
        mCartTotal.setText("$" + String.format("%.2f", cartTotal));
        return cartTotal;
    }

    private double getVatTotal() {
        vatTotal = totalPrice * 0.12;
        mVatTaxTotal.setText(String.format("%.2f", vatTotal));
        return vatTotal;
    }

    public double getSubTotal() {
        totalPrice = 0.0d;
        for (int i = 0; i < mCartList.size(); i++) {
            totalPrice += (Double.parseDouble(mCartList.get(i).getItemPrice()) * mCartList.get(i).getNoOfItems());
        }
        mSubTotal.setText(String.format("%.2f", totalPrice));
        return totalPrice;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_button: {
                    getParentFragmentManager().beginTransaction().remove(mCartInformationFragment).commit();
                    break;

                }
                case R.id.check_out_button: {
                    Toast.makeText(getContext(), "Checkout has been clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.remove_all_items: {
                    showRemoveAllDialog();
                    break;
                }
            }
        }
    };

    private void showRemoveAllDialog() {
        new AlertDialog.Builder(getContext())
                //.setTitle("Your Cart")
                .setMessage("Remove all items from your Cart?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        cartDB.cartDao().deleteAllItems();
                        loadProgressDialog();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadCartList();
                            }
                        }, 1000);
                    }
                }).create().show();
    }

    private void showRemoveDialog(int cartID) {
        new AlertDialog.Builder(getContext())
                //.setTitle("Your Cart")
                .setMessage("Remove to Cart?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        cartDB.cartDao().deleteFromCart(cartID);
                        loadProgressDialog();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadCartList();
                            }
                        }, 1000);
                    }
                }).create().show();
    }

    @Override
    public boolean onBackPressed() {
        if (mCartInformationFragment != null && mCartInformationFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mCartInformationFragment).commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refreshCartList() {
        loadCartList();
    }
}