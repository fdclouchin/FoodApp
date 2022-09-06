package com.example.foodapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.FoodInformationFragment;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private static final String FOOD_TITLE = "FOOD_TITLE";
    private static final String FOOD_PRICE = "FOOD_PRICE";
    private static final String FOOD_DESCRIPTION = "FOOD_DESCRIPTION";
    private static final String FOOD_IMG = "FOOD_IMAGE";

    private ArrayList<Foods> mPopularList;
    private final Context mContext;

    public PopularAdapter(ArrayList<Foods> popularList, Context context) {
        this.mPopularList = popularList;
        this.mContext = context;
    }

    public void setFilteredList(ArrayList<Foods> filteredList) {
        this.mPopularList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foods_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        holder.mFoodTitle.setText(mPopularList.get(position).getTitle());
        holder.mFoodPrice.setText(String.valueOf(mPopularList.get(position).getFee()));

        int drawableResourceID = holder.itemView.getContext().getResources().getIdentifier
                (mPopularList.get(position).getPic(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceID)
                .into(holder.mFoodImage);

        String foodTitle = mPopularList.get(position).getTitle();
        String foodPrice = String.valueOf(mPopularList.get(position).getFee());
        String foodDescription = mPopularList.get(position).getDescription();
        String foodImage = mPopularList.get(position).getPic();

        holder.mPopularLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString(FOOD_TITLE, foodTitle);
                bundle.putString(FOOD_PRICE, foodPrice);
                bundle.putString(FOOD_DESCRIPTION, foodDescription);
                bundle.putString(FOOD_IMG, foodImage);
                FoodInformationFragment passBundle = new FoodInformationFragment();
                passBundle.setArguments(bundle);
                displayFoodItem(passBundle);
            }
        });
    }

    private void displayFoodItem(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.display_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return mPopularList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mFoodTitle;
        private final ImageView mFoodImage;
        private final TextView mFoodPrice;
        private final ConstraintLayout mPopularLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodTitle = itemView.findViewById(R.id.food_title);
            mFoodImage = itemView.findViewById(R.id.food_image);
            mFoodPrice = itemView.findViewById(R.id.food_price);
            mPopularLayout = itemView.findViewById(R.id.popular_layout);
        }
    }
}
