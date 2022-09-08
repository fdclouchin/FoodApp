package com.example.foodapp.Adapters;

import android.content.Context;
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
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.CartDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    private ArrayList<Cart> mCart;
    private Context mContext;
    private RemoveCartItem mCartCallback;

    public CartAdapter(Context context, RemoveCartItem cartCallback) {
        this.mContext = context;
        this.mCartCallback = cartCallback;
    }

    public void setCartList (ArrayList<Cart> cartList) {
        this.mCart = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.mPosition.setText(String.valueOf(mCart.get(position).cart_id));
        holder.mTitleItem.setText(mCart.get(position).itemTitle);
        holder.mPriceItem.setText(mCart.get(position).itemPrice);

        String foodPrice = mCart.get(position).itemPrice;
        int noOfItems = mCart.get(position).noOfItems;
        String foodImage = mCart.get(position).itemImage;
        holder.mPriceItem.setText(foodPrice + " x " + noOfItems);

        int drawableResourceID = mContext.getResources().getIdentifier(foodImage, "drawable", mContext.getPackageName());
        Glide.with(mContext)
                .load(drawableResourceID)
                .into(holder.mImageItem);

        Double price = Double.valueOf(foodPrice);
        //Double totalPrice = (price * noOfItems);
        String totalPriceConverted = String.format("%.2f", (price * noOfItems));
        holder.mTotalPrice.setText(totalPriceConverted);
        int itemPosition = mCart.get(position).cart_id;

        holder.mCartItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "Long pressed cart_id " + itemPosition, Toast.LENGTH_SHORT).show();
                //removeFromCart(itemPosition);
                mCartCallback.removeItem(itemPosition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mCart == null)? 0: mCart.size();
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
            mPosition= itemView.findViewById(R.id.cart_position);
            mImageItem = itemView.findViewById(R.id.summary_image);
            mTitleItem = itemView.findViewById(R.id.summary_item_title);
            mPriceItem = itemView.findViewById(R.id.summary_no_of_items);
            mTotalPrice = itemView.findViewById(R.id.summary_total_per_item);
            mCartItemLayout = itemView.findViewById(R.id.cart_item_layout);
        }
    }

    public interface RemoveCartItem {
        void removeItem(int id);
    }
}
