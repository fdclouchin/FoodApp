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
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.Helper.MinMaxFilter;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.CartDatabase;

public class FoodInformationFragment extends Fragment implements OnBackPressedFragment {

    private static final String FOOD_TITLE = "FOOD_TITLE";
    private static final String FOOD_PRICE = "FOOD_PRICE";
    private static final String FOOD_DESCRIPTION = "FOOD_DESCRIPTION";
    private static final String FOOD_IMG = "FOOD_IMAGE";
    //private static final String NO_OF_ITEMS = "NO_OF_ITEMS";

    private TextView mFoodTitle;
    private TextView mFoodPrice;
    private TextView mFoodDescription;
    private RelativeLayout mCloseModal;
    private TextView mInfoAddToCart;

    private ImageView mSubtract;
    private ImageView mAdd;
    private EditText mItemCount;

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

        mFoodTitle = view.findViewById(R.id.food_edit_title_view);
        mFoodPrice = view.findViewById(R.id.food_edit_price_view);
        mFoodDescription = view.findViewById(R.id.food_edit_descriptions_view);
        mCloseModal = view.findViewById(R.id.close_edit_modal);
        mInfoAddToCart = view.findViewById(R.id.add_to_cart_edit_info_button);
        mInfoFoodImage = view.findViewById(R.id.food_edit_info_image);
        mSubtract = view.findViewById(R.id.subtract_edit_item);
        mAdd = view.findViewById(R.id.add_edit_item);
        mItemCount = view.findViewById(R.id.item_edit_count);
        //set minimum/maximum values
        mItemCount.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});

        mCloseModal.setOnClickListener(mOnClickListener);
        mInfoAddToCart.setOnClickListener(mOnClickListener);
        mSubtract.setOnClickListener(mOnClickListener);
        mAdd.setOnClickListener(mOnClickListener);

        checkItemCount();

        foodInfoFragment = (FoodInformationFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.display_fragment);
        getBundles();
        return view;
    }

    private void checkItemCount() {
        String itemCountValue = mItemCount.getText().toString();
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
                    Toast.makeText(getActivity(), "Close", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction().remove(foodInfoFragment).commit();
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mItemCount.getWindowToken(), 0);
                    break;
                }
                case R.id.add_to_cart_edit_info_button: {
                    if (mItemCount != null) {
                        Toast.makeText(getActivity(), "Add to cart", Toast.LENGTH_SHORT).show();

                        String foodTitle = mFoodTitle.getText().toString();
                        String foodPrice = mFoodPrice.getText().toString();
                        int itemCount = Integer.parseInt(mItemCount.getText().toString());
                        Bundle bundle = FoodInformationFragment.this.getArguments();
                        assert bundle != null;
                        String foodImage = bundle.getString(FOOD_IMG);

                        addToCart(foodTitle, foodPrice, itemCount,foodImage);
                    }
                    break;
                }
                case R.id.subtract_edit_item: {
                    String itemCountValue = mItemCount.getText().toString();
                    int itemCount = Integer.parseInt(itemCountValue);
                    itemCount--;
                    mItemCount.setText(Integer.toString(itemCount));
                    checkItemCount();
                    break;
                }
                case R.id.add_edit_item: {
                    String itemCountValue = mItemCount.getText().toString();
                    int itemCount = Integer.parseInt(itemCountValue);
                    itemCount++;
                    mItemCount.setText(Integer.toString(itemCount));
                    checkItemCount();
                    break;
                }
            }
        }
    };

    private void addToCart(String itemTitle, String itemPrice, int noOfItems, String itemImage) {
        CartDatabase db = CartDatabase.getDbInstance(requireActivity());

        Cart cart = new Cart();
        cart.itemTitle = itemTitle;
        cart.itemPrice = itemPrice;
        cart.noOfItems = noOfItems;
        cart.itemImage = itemImage;
        db.cartDao().addToCart(cart);
    }

    private void getBundles() {
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
    public boolean onBackPressed() {
        if (foodInfoFragment != null && foodInfoFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(foodInfoFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}