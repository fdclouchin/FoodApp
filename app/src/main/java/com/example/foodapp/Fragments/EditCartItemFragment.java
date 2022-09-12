package com.example.foodapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Helper.MinMaxFilter;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.CartDatabase;

public class EditCartItemFragment extends Fragment implements OnBackPressedFragment {
    private static final String CART_ID = "CART_ID";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String ITEM_PRICE = "ITEM_PRICE";
    private static final String ITEM_IMAGE = "ITEM_IMAGE";
    private static final String NO_OF_ITEMS = "NO_OF_ITEMS";

    private EditCartItemFragment mEditCartItemFragment;
    private RefreshCart mCallback;
    private RelativeLayout mCloseEditCartItem;

    private ImageView mSubtract;
    private ImageView mAdd;
    private EditText mItemEditCount;
    private TextView mUpdateItemCountButton;

    private TextView mEditFoodTitle;
    private TextView mEditFoodPrice;
    private ImageView mFoodImage;

    private CartDatabase cartDB;

    private ProgressDialog mProgressDialog;

    public EditCartItemFragment() {
    }

    public void setRefreshCartCallback(RefreshCart refreshCart) {
        this.mCallback = refreshCart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cart_item, container, false);

        mCloseEditCartItem = view.findViewById(R.id.close_edit_modal);
        mSubtract = view.findViewById(R.id.subtract_edit_item);
        mAdd = view.findViewById(R.id.add_edit_item);
        mItemEditCount = view.findViewById(R.id.item_edit_count);
        mUpdateItemCountButton = view.findViewById(R.id.add_to_cart_edit_info_button);
        mEditFoodTitle = view.findViewById(R.id.food_edit_title);
        mEditFoodPrice = view.findViewById(R.id.food_edit_price);
        mFoodImage = view.findViewById(R.id.food_edit_info_image);

        //set minimum/maximum values
        mItemEditCount.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});

        mCloseEditCartItem.setOnClickListener(mOnClickListener);
        mUpdateItemCountButton.setOnClickListener(mOnClickListener);
        mSubtract.setOnClickListener(mOnClickListener);
        mAdd.setOnClickListener(mOnClickListener);

        mProgressDialog = new ProgressDialog(getContext());
        cartDB = CartDatabase.getDbInstance(getContext().getApplicationContext());

        mEditCartItemFragment = (EditCartItemFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.display_fragment);
        getUpdateBundles();
        checkItemCount();
        return view;
    }

    private void getUpdateBundles() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String foodEditTitle = bundle.getString(ITEM_NAME);
            String foodEditPrice = bundle.getString(ITEM_PRICE);
            int foodEditNumberOfItems = bundle.getInt(NO_OF_ITEMS);
            String foodImage = bundle.getString(ITEM_IMAGE);

            mEditFoodTitle.setText(foodEditTitle);
            mEditFoodPrice.setText(foodEditPrice);
            mItemEditCount.setText(String.valueOf(foodEditNumberOfItems));

            int drawableResourceID = getContext()
                    .getResources()
                    .getIdentifier(foodImage, "drawable", getContext()
                            .getPackageName());
            Glide.with(getActivity())
                    .load(drawableResourceID)
                    .into(mFoodImage);
        }
    }

    private void checkItemCount() {
        String itemCountValue = mItemEditCount.getText().toString();
        int itemCount = Integer.parseInt(itemCountValue);
        if (itemCount == 1) {
            mSubtract.setEnabled(false);
        } else if (itemCount > 1) {
            mSubtract.setEnabled(true);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_edit_modal: {
                    closeEditCartModal();
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mItemEditCount.getWindowToken(), 0);
                    break;
                }
                case R.id.subtract_edit_item: {
                    String itemCountValue = mItemEditCount.getText().toString();
                    int itemCount = Integer.parseInt(itemCountValue);
                    itemCount--;
                    mItemEditCount.setText(Integer.toString(itemCount));
                    checkItemCount();
                    break;
                }
                case R.id.add_edit_item: {
                    String itemCountValue = mItemEditCount.getText().toString();
                    int itemCount = Integer.parseInt(itemCountValue);
                    itemCount++;
                    mItemEditCount.setText(Integer.toString(itemCount));
                    checkItemCount();
                    break;
                }
                case R.id.add_to_cart_edit_info_button: {
                    Bundle bundle = getArguments();
                    int foodCartID = bundle.getInt(CART_ID);
                    int noOfItems = Integer.parseInt(mItemEditCount.getText().toString());

                    mProgressDialog.setTitle(R.string.loading_label);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                    //update cart item
                    cartDB.cartDao().updateData(noOfItems, foodCartID);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            closeEditCartModal();
                            mCallback.refreshCartList();
                        }
                    }, 1500);
                }
            }
        }
    };

    private void closeEditCartModal() {
        getParentFragmentManager()
                .beginTransaction()
                .remove(mEditCartItemFragment)
                .commit();
    }

    @Override
    public boolean onBackPressed() {
        if (mEditCartItemFragment != null && mEditCartItemFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mEditCartItemFragment).commit();
            return true;
        } else {
            return false;
        }
    }

    public interface RefreshCart {
        void refreshCartList();
    }
}