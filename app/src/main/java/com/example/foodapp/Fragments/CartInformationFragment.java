package com.example.foodapp.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
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
import com.example.foodapp.Interfaces.ButtonClickListener;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartInformationFragment extends Fragment implements OnBackPressedFragment, CartAdapter.RemoveCartItem {

    private final String mActionBarTitle;
    private CartInformationFragment mCartInformationFragment;
    private ImageView mBackButton;
    private TextView mTitle;

    private RecyclerView mCartRecyclerView;
    private CartAdapter mCartAdapter;
    private TextView mCartSize;
    private ProgressDialog mDialog;
    private ArrayList<Cart> mCartList;

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

        mTitle.setText(mActionBarTitle);
        mBackButton.setOnClickListener(mOnClickListener);
        mDialog = new ProgressDialog(getContext());

        mCartInformationFragment = (CartInformationFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.display_fragment);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        mCartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mCartRecyclerView.addItemDecoration(dividerItemDecoration);

        mCartAdapter = new CartAdapter(getContext(), mCartInformationFragment);
        mCartRecyclerView.setAdapter(mCartAdapter);


        SwipeHelper swipeHelper = new SwipeHelper(getContext(), mCartRecyclerView, 230) {
            @Override
            public void instantiateItemButton(RecyclerView.ViewHolder viewHolder, List<ItemButton> buffer) {
                buffer.add(new ItemButton(
                        viewHolder.getAdapterPosition(),
                        getContext(),
                        getString(R.string.delete_label).toUpperCase(),
                        0,
                        50,
                        Color.parseColor("#FF3C30"),
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
                        viewHolder.getAdapterPosition(),
                        getContext(),
                        getString(R.string.edit_label).toUpperCase(),
                        0,
                        50,
                        Color.parseColor("#FF9502"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Cart data = mCartAdapter.getItem(position);
                                int cartID = Integer.parseInt(String.valueOf(data.cart_id));

                                EditCartItemFragment editCartItemFragment = new EditCartItemFragment();
                                displayEditCartItem(editCartItemFragment);

                                Toast.makeText(getContext(), "EDIT?" + position + " Cart ID: " + cartID, Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
            }
        };

        loadProgressDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCartList();
            }
        }, 1000);
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
        initDB();
        if (mCartList != null) {
            mCartAdapter.setCartList(mCartList);
        }
        mCartSize.setText("(" + mCartList.size() + ")");

        mDialog.dismiss();
    }

    private void initDB() {
        CartDatabase db = CartDatabase.getDbInstance(getContext().getApplicationContext());
        mCartList = (ArrayList<Cart>) db.cartDao().getAllCart();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getParentFragmentManager().beginTransaction().remove(mCartInformationFragment).commit();
        }
    };

    @Override
    public void removeItem(int cartID) {
        showRemoveDialog(cartID);
    }

    private void showRemoveDialog(int cartID) {
        new AlertDialog.Builder(getContext())
                //.setTitle("Your Cart")
                .setMessage("Remove to Cart?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        CartDatabase db = CartDatabase.getDbInstance(getContext());
                        db.cartDao().deleteFromCart(cartID);
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
}