package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapters.CategoryAdapter;
import com.example.foodapp.Adapters.PopularAdapter;
import com.example.foodapp.Fragments.CartInformationFragment;
import com.example.foodapp.Fragments.SettingsFragment;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.Model.Category;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnClickCategory {

    private RecyclerView mCategoryListRecyclerView;
    private RecyclerView mPopularListRecyclerView;
    private ArrayList<Foods> mPopularList;
    private ArrayList<Category> mCategoryList;

    private PopularAdapter mPopularAdapter;
    private CategoryAdapter mCategoryAdapter;

    private TextView mOrderNow;
    private TextView mClearFilter;

    private SearchView mSearch;
    private FloatingActionButton mCart;

    private LinearLayout mHomeButton;
    private LinearLayout mProfileButton;
    private LinearLayout mSupportButton;
    private LinearLayout mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize
        mCategoryListRecyclerView = findViewById(R.id.category_lists);
        mPopularListRecyclerView = findViewById(R.id.popular_lists);

        mOrderNow = findViewById(R.id.order_now_button);
        mClearFilter = findViewById(R.id.clear_filter);
        mCart = findViewById(R.id.cart_floating_button);
        mSearch = findViewById(R.id.search_food);

        mHomeButton = findViewById(R.id.home_button);
        mProfileButton = findViewById(R.id.profile_button);
        mSupportButton = findViewById(R.id.support_button);
        mSettingsButton = findViewById(R.id.settings_button);

        mSearch.clearFocus();

        mOrderNow.setOnClickListener(mOnClickListener);
        mClearFilter.setOnClickListener(mOnClickListener);
        mCart.setOnClickListener(mOnClickListener);

        mHomeButton.setOnClickListener(mOnClickListener);
        mProfileButton.setOnClickListener(mOnClickListener);
        mSupportButton.setOnClickListener(mOnClickListener);
        mSettingsButton.setOnClickListener(mOnClickListener);

        displayCategoryList();
        displayPopularList();

        //will not push bottom nav bar if soft keyboard is visible
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING); ||  android:windowSoftInputMode="adjustNothing"

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

    }

    private void filterList(String text) {
        ArrayList<Foods> filteredList = new ArrayList<>();
        for (Foods foods : mPopularList) {
            if (foods.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(foods);
            } else if (foods.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(foods);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
        } else {
            mPopularAdapter.setFilteredList(filteredList);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_now_button: {
                    Toast.makeText(MainActivity.this, "Order Now has been clicked", Toast.LENGTH_SHORT).show();
                    displayPopularListTest();
                    break;
                }
                case R.id.clear_filter: {
                    displayPopularList();
                    break;
                }
                case R.id.cart_floating_button: {
                    CartInformationFragment cartInformationFragment = new CartInformationFragment("Your Cart");
                    Fragment cartFragment = getSupportFragmentManager().findFragmentByTag("CartFragmentTag");
                    //only display fragment if not displayed yet
                    if (cartFragment == null) {
                        displayCartFragment(cartInformationFragment);
                    }
                    break;
                }
                case R.id.home_button: {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.display_fragment);
                    if (fragment != null) {
                        onBackPressed();
                    }
                }
                case R.id.settings_button: {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    Fragment setFragment = getSupportFragmentManager().findFragmentByTag("SettingsFragmentTag");
                    if (setFragment == null) {
                        displaySettingsFragment(settingsFragment);
                    }
                }
            }
        }
    };

    private void displaySettingsFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.display_fragment, fragment, "SettingsFragmentTag")
                .commit();
    }

    private void displayCartFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.display_fragment, fragment, "CartFragmentTag")
                .commit();
    }

    private void displayPizzaCategory() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Pepperoni Pizza", "pizza", "sinigang na pizza with olive oil", 29.99));
        mPopularList.add(new Foods("Vegetable Pizza w/ sinigang burger", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void displayBurgerCategory() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Cheese Burger", "pop_2", "sinigang na burger with soy sauce", 7.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void displayHotdogCategory() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Hotdog with sinigang na pizza", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));
        mPopularList.add(new Foods("Fishda with a twist", "fish_1", "sinigang na burger with ketchup and pizza dahon", 32.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void displayDrinkCategory() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Coffee", "coffee_1", "Coffee", 15.75));
        mPopularList.add(new Foods("SUPER DRINKS", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void displayDonutCategory() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Donut with sinigang na kape", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void displayPopularListTest() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Pepperoni Pizza", "pizza", "sinigang na pizza with olive oil", 29.99));

        setFoodAdapter();
        ifEmptyList();
    }

    private void ifEmptyList() {
        if (mPopularList.size() == 0) {
            Toast.makeText(this, "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPopularList() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Pepperoni Pizza", "pizza", "sinigang na pizza with olive oil", 29.99));
        mPopularList.add(new Foods("Cheese Burger", "pop_2", "sinigang na burger with soy sauce", 7.99));
        mPopularList.add(new Foods("Coffee", "coffee_1", "Coffee", 15.75));
        mPopularList.add(new Foods("Vegetable Pizza w/ sinigang burger", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));
        mPopularList.add(new Foods("Fishdaa", "fish_1", "Fishdaaaaa", 42.99));
        mPopularList.add(new Foods("Cat Pizza w/ sinigang burger", "pop_3", "sinigang na burger with ketchup and pizza dahon", 52.99));
        mPopularList.add(new Foods("Cow Pizza w/ sinigang burger", "pop_2", "sinigang na burger with ketchup and pizza dahon", 62.99));
        mPopularList.add(new Foods("Isda with a twist", "fish_1", "sinigang na burger with ketchup and pizza dahon", 72.99));
        mPopularList.add(new Foods("Dahon Pizza w/ sinigang burger", "pop_1", "sinigang na burger with ketchup and pizza dahon", 82.99));
        mPopularList.add(new Foods("Car Pizza w/ sinigang burger", "pop_2", "sinigang na burger with ketchup and pizza dahon", 92.99));
        mPopularList.add(new Foods("Coffee Pizza w/ sinigang burger", "fish_1", "sinigang na burger with ketchup and pizza dahon", 12.99));

        setFoodAdapter();
    }

    private void setFoodAdapter() {
        mPopularAdapter = new PopularAdapter(mPopularList, MainActivity.this);
        mPopularListRecyclerView.setAdapter(mPopularAdapter);
    }

    private void displayCategoryList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new Category("Pizza", "cat_1"));
        mCategoryList.add(new Category("Burger", "cat_2"));
        mCategoryList.add(new Category("Hotdog", "cat_3"));
        mCategoryList.add(new Category("Drink", "cat_4"));
        mCategoryList.add(new Category("Donut", "cat_5"));

        setCategoryList();
    }

    private void setCategoryList() {
        mCategoryAdapter = new CategoryAdapter(mCategoryList, MainActivity.this);
        mCategoryListRecyclerView.setAdapter(mCategoryAdapter);
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.display_fragment);
        if (!(displayFragment instanceof OnBackPressedFragment) || !((OnBackPressedFragment) displayFragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void clickCategory(String catTitle) {
        switch (catTitle) {
            case "Pizza": {
                displayPizzaCategory();
                break;
            }
            case "Burger": {
                displayBurgerCategory();
                break;
            }
            case "Hotdog": {
                displayHotdogCategory();
                break;
            }
            case "Drink": {
                displayDrinkCategory();
                break;
            }
            case "Donut": {
                displayDonutCategory();
                break;
            }
        }
    }
}