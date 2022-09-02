package com.example.foodapp.Adapters;

import android.content.Context;
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
    private ArrayList<Foods> mPopularList;
    private Context mContext;

    public PopularAdapter(ArrayList<Foods> popularList, Context context) {
        this.mPopularList = popularList;
        this.mContext = context;
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
        holder.mPopularLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Item " + holder.getAdapterPosition() + " has been selected", Toast.LENGTH_SHORT).show();
                displayFoodItem(new FoodInformationFragment());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mFoodTitle;
        private ImageView mFoodImage;
        private TextView mFoodPrice;
        private ConstraintLayout mPopularLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodTitle = itemView.findViewById(R.id.food_title);
            mFoodImage = itemView.findViewById(R.id.food_image);
            mFoodPrice = itemView.findViewById(R.id.food_price);
            mPopularLayout = itemView.findViewById(R.id.popular_layout);
        }
    }
}
