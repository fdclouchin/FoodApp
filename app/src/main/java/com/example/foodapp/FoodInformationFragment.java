package com.example.foodapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class FoodInformationFragment extends Fragment implements OnBackPressedFragment{

    private static final String FOOD_TITLE = "FOOD_TITLE";
    private static final String FOOD_PRICE = "FOOD_PRICE";
    private static final String FOOD_DESCRIPTION = "FOOD_DESCRIPTION";

    private TextView mFoodTitle;
    private TextView mFoodPrice;
    private TextView mFoodDescription;

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

        getBundles();
        return view;
    }

    private void getBundles() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String foodTitle = bundle.getString(FOOD_TITLE);
            String foodPrice = bundle.getString(FOOD_PRICE);
            String foodDescription = bundle.getString(FOOD_DESCRIPTION);
            mFoodTitle.setText(foodTitle);
            mFoodPrice.setText(foodPrice);
            mFoodDescription.setText(foodDescription);
        }
    }

    @Override
    public boolean onBackPressed() {
        FoodInformationFragment foodInfoFragment = (FoodInformationFragment)
                requireActivity().getSupportFragmentManager().findFragmentById(R.id.display_fragment);

        if (foodInfoFragment !=null && foodInfoFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(foodInfoFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}