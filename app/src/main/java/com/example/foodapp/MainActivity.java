package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapters.CategoryAdapter;
import com.example.foodapp.Adapters.PopularAdapter;
import com.example.foodapp.Model.Category;
import com.example.foodapp.Model.Foods;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mCategoryListRecyclerView;
    private ArrayList<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;

    private RecyclerView mPopularListRecyclerView;
    private ArrayList<Foods> mPopularList;
    private PopularAdapter mPopularAdapter;

    private TextView mOrderNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize
        mCategoryListRecyclerView = findViewById(R.id.category_lists);
        mPopularListRecyclerView = findViewById(R.id.popular_lists);

        mOrderNow = findViewById(R.id.order_now_button);
        mOrderNow.setOnClickListener(mOnClickListener);

        displayCategoryList();
        displayPopularList();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_now_button: {
                    Toast.makeText(MainActivity.this, "Order Now has been clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    private void displayPopularList() {
        mPopularList = new ArrayList<>();
        mPopularList.add(new Foods("Pepperoni Pizza", "pizza", "sinigang na pizza with olive oil", 29.99));
        mPopularList.add(new Foods("Cheese Burger", "pop_2", "sinigang na burger with soy sauce", 7.99));
        mPopularList.add(new Foods("Vegetable Pizza w/ sinigang burger", "pop_1", "sinigang na burger with ketchup and pizza dahon", 32.99));

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

        mCategoryAdapter = new CategoryAdapter(mCategoryList, MainActivity.this);
        mCategoryListRecyclerView.setAdapter(mCategoryAdapter);
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}