package com.example.foodapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.Helper.MinMaxFilter;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.R;

public class EditCartItemFragment extends Fragment implements OnBackPressedFragment {
    private static final String CART_ID = "CART_ID";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String ITEM_IMAGE = "ITEM_IMAGE";
    private static final String NO_OF_ITEMS = "NO_OF_ITEMS";

    private EditCartItemFragment mEditCartItemFragment;
    private RelativeLayout mCloseEditCartItem;

    private ImageView mSubtract;
    private ImageView mAdd;
    private EditText mItemEditCount;
    private TextView mUpdateItemCount;

    public EditCartItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cart_item, container, false);
        mCloseEditCartItem = view.findViewById(R.id.close_edit_modal);
        mSubtract = view.findViewById(R.id.subtract_edit_item);
        mAdd = view.findViewById(R.id.add_edit_item);
        mItemEditCount = view.findViewById(R.id.item_edit_count);
        mUpdateItemCount = view.findViewById(R.id.add_to_cart_edit_info_button);
        //set minimum/maximum values
        mItemEditCount.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});

        mCloseEditCartItem.setOnClickListener(mOnClickListener);
        mUpdateItemCount.setOnClickListener(mOnClickListener);
        mSubtract.setOnClickListener(mOnClickListener);
        mAdd.setOnClickListener(mOnClickListener);

        checkItemCount();

        mEditCartItemFragment = (EditCartItemFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.display_fragment);
        getUpdateBundles();
        return view;
    }

    private void getUpdateBundles() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String foodEditTitle = bundle.getString(CART_ID);
            String foodEditPrice = bundle.getString(ITEM_NAME);
            String foodEditNumberOfItems = bundle.getString(NO_OF_ITEMS);
            String foodImage = bundle.getString(ITEM_IMAGE);

            /*mFoodTitle.setText(foodTitle);
            mFoodPrice.setText(foodPrice);
            mFoodDescription.setText(foodDescription);

            int drawableResourceID = getContext().getResources().getIdentifier(foodImage, "drawable", getContext().getPackageName());
            Glide.with(getActivity())
                    .load(drawableResourceID)
                    .into(mInfoFoodImage);*/
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
                    getParentFragmentManager().beginTransaction().remove(mEditCartItemFragment).commit();
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
                    //TODO
                    Toast.makeText(getActivity(), "Update The ITEM", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        if (mEditCartItemFragment != null && mEditCartItemFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mEditCartItemFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}