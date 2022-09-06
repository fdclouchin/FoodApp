package com.example.foodapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class FoodInformationFragment extends Fragment implements OnBackPressedFragment {

    private static final String FOOD_TITLE = "FOOD_TITLE";
    private static final String FOOD_PRICE = "FOOD_PRICE";
    private static final String FOOD_DESCRIPTION = "FOOD_DESCRIPTION";
    private static final String FOOD_IMG = "FOOD_IMAGE";

    private TextView mFoodTitle;
    private TextView mFoodPrice;
    private TextView mFoodDescription;
    private RelativeLayout mCloseModal;
    private TextView mInfoAddToCart;

    private FoodInformationFragment foodInfoFragment;
    private ImageView mInfoFoodImage;
    public FoodInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_information, container, false);

        mFoodTitle = view.findViewById(R.id.food_title_view);
        mFoodPrice = view.findViewById(R.id.food_price_view);
        mFoodDescription = view.findViewById(R.id.food_descriptions_view);
        mCloseModal = view.findViewById(R.id.close_modal);
        mInfoAddToCart = view.findViewById(R.id.add_to_cart_info_button);
        mInfoFoodImage = view.findViewById(R.id.food_info_image);

        mCloseModal.setOnClickListener(mOnClickListener);
        mInfoAddToCart.setOnClickListener(mOnClickListener);

        foodInfoFragment = (FoodInformationFragment)
                requireActivity().getSupportFragmentManager().findFragmentById(R.id.display_fragment);

            getBundles();
            return view;
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.close_modal: {
                        Toast.makeText(getActivity(), "Close", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction().remove(foodInfoFragment).commit();
                        break;
                    }
                    case R.id.add_to_cart_info_button: {
                        Toast.makeText(getActivity(), "Add to cart", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };

        private void getBundles () {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                String foodTitle = bundle.getString(FOOD_TITLE);
                String foodPrice = bundle.getString(FOOD_PRICE);
                String foodDescription = bundle.getString(FOOD_DESCRIPTION);
                String foodImage = bundle.getString(FOOD_IMG);

                mFoodTitle.setText(foodTitle);
                mFoodPrice.setText(foodPrice);
                mFoodDescription.setText(foodDescription);

                int drawableResourceID = getContext().getResources().getIdentifier(foodImage, "drawable", getContext().getPackageName());
                Glide.with(getActivity())
                        .load(drawableResourceID)
                        .into(mInfoFoodImage);
            }
        }

        @Override
        public boolean onBackPressed () {

            if (foodInfoFragment != null && foodInfoFragment.isVisible()) {
                getParentFragmentManager().beginTransaction().remove(foodInfoFragment).commit();
                return true;
            } else {
                return false;
            }
        }
    }