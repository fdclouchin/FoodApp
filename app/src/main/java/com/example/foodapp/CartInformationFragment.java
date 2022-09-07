package com.example.foodapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class CartInformationFragment extends Fragment implements OnBackPressedFragment {
    private static final String FOOD_TITLE = "FOOD_TITLE";
    private static final String FOOD_PRICE = "FOOD_PRICE";
    private static final String FOOD_IMG = "FOOD_IMAGE";
    private static final String NO_OF_ITEMS = "NO_OF_ITEMS";

    private final String mActionBarTitle;

    private CartInformationFragment cartInformationFragment;
    private ImageView mBackButton;
    private TextView mTitle;
    private ImageView mImageItem;
    private TextView mTitleItem;
    private TextView mPriceItem;
    private TextView mTotalPrice;



    public CartInformationFragment(String title) {
        this.mActionBarTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_information, container, false);
        mBackButton = view.findViewById(R.id.back_button);
        mTitle = view.findViewById(R.id.text_view_title);
        mImageItem = view.findViewById(R.id.summary_image);
        mTitleItem = view.findViewById(R.id.summary_item_title);
        mPriceItem = view.findViewById(R.id.summary_no_of_items);
        mTotalPrice = view.findViewById(R.id.summary_total_per_item);

        mTitle.setText(mActionBarTitle);
        mBackButton.setOnClickListener(mOnClickListener);
        cartInformationFragment = (CartInformationFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.display_fragment);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String foodTitle = bundle.getString(FOOD_TITLE);
            String foodPrice = bundle.getString(FOOD_PRICE);
            String foodImage = bundle.getString(FOOD_IMG);
            String foodItem = bundle.getString(NO_OF_ITEMS);

            mTitleItem.setText(foodTitle);
            mPriceItem.setText(foodPrice + " x " + foodItem);

            int drawableResourceID = getContext().getResources().getIdentifier(foodImage, "drawable", getContext().getPackageName());
            Glide.with(getActivity())
                    .load(drawableResourceID)
                    .into(mImageItem);

            Double price = Double.valueOf(foodPrice);
            int noOfItems = Integer.parseInt(foodItem);
            Double totalPrice = (price * noOfItems);

            mTotalPrice.setText(totalPrice.toString());
        }
        return view;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getParentFragmentManager().beginTransaction().remove(cartInformationFragment).commit();
        }
    };

    @Override
    public boolean onBackPressed() {
        if (cartInformationFragment != null && cartInformationFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(cartInformationFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}