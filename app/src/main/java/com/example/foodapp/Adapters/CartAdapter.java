package com.example.foodapp.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    private final Context mContext;

    private ArrayList<Cart> mCartList;

    public CartAdapter(Context context) {
        this.mContext = context;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.mCartList = cartList;
        notifyDataSetChanged();
    }

    public Cart getItem(int position) {
        return mCartList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        RecyclerView.ViewHolder vh = new VH(view);
        return (VH) vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.mPosition.setText(String.valueOf(mCartList.get(position).cart_id));
        holder.mTitleItem.setText(mCartList.get(position).itemTitle);
        holder.mPriceItem.setText(mCartList.get(position).itemPrice);

        String foodPrice = mCartList.get(position).itemPrice;
        int noOfItems = mCartList.get(position).noOfItems;
        String foodImage = mCartList.get(position).itemImage;
        holder.mPriceItem.setText(foodPrice + " x " + noOfItems);

        int drawableResourceID = mContext.getResources().getIdentifier(foodImage, "drawable", mContext.getPackageName());
        Glide.with(mContext)
                .load(drawableResourceID)
                .into(holder.mImageItem);

        Double price = Double.valueOf(foodPrice);
        String totalPriceConverted = String.format("%.2f", (price * noOfItems));
        holder.mTotalPrice.setText("$ " + totalPriceConverted);
    }

    @Override
    public int getItemCount() {
        return (mCartList == null) ? 0 : mCartList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView mPosition;
        private ImageView mImageItem;
        private TextView mTitleItem;
        private TextView mPriceItem;
        private TextView mTotalPrice;
        private ConstraintLayout mCartItemLayout;

        public VH(@NonNull View itemView) {
            super(itemView);
            mPosition = itemView.findViewById(R.id.cart_position);
            mImageItem = itemView.findViewById(R.id.summary_image);
            mTitleItem = itemView.findViewById(R.id.summary_item_title);
            mPriceItem = itemView.findViewById(R.id.summary_no_of_items);
            mTotalPrice = itemView.findViewById(R.id.summary_total_per_item);
            mCartItemLayout = itemView.findViewById(R.id.cart_item_layout);
        }
    }

    /*public String getSafeSubstring(String s, int maxLength){
        if(!TextUtils.isEmpty(s)){
            if(s.length() >= maxLength){
                return s.substring(0, maxLength);
            }
        }
        return s;
    }*/
}
