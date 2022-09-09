package com.example.foodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapters.CartAdapter;
import com.example.foodapp.Helper.SwipeHelper;
import com.example.foodapp.Interfaces.ButtonClickListener;
import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.RoomDatabase.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartInformationFragment extends Fragment implements OnBackPressedFragment, CartAdapter.RemoveCartItem {

    private final String mActionBarTitle;
    private CartInformationFragment mCartInformationFragment;
    private ImageView mBackButton;
    private TextView mTitle;

    private RecyclerView mCartRecyclerView;
    private CartAdapter mCartAdapter;
    private TextView mCartSize;

    public CartInformationFragment(String title) {
        this.mActionBarTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_information, container, false);

        mBackButton = view.findViewById(R.id.back_button);
        mTitle = view.findViewById(R.id.text_view_title);
        mCartSize = view.findViewById(R.id.cart_size);

        mTitle.setText(mActionBarTitle);
        mBackButton.setOnClickListener(mOnClickListener);

        mCartInformationFragment = (CartInformationFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.display_fragment);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        mCartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mCartRecyclerView.addItemDecoration(dividerItemDecoration);

        mCartAdapter = new CartAdapter(getContext(), mCartInformationFragment);
        mCartRecyclerView.setAdapter(mCartAdapter);

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), mCartRecyclerView, 200) {
            @Override
            public void instantiateItemButton(RecyclerView.ViewHolder viewHolder, List<ItemButton> buffer) {
                buffer.add(new ItemButton(
                        viewHolder.getAdapterPosition(),
                        getContext(),
                        "Delete",
                        R.drawable.ic_delete,
                        30,
                        Color.parseColor("#FF3C30"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Toast.makeText(getContext(), "Delete? ", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
                buffer.add(new ItemButton(
                        viewHolder.getAdapterPosition(),
                        getContext(),
                        "Update",
                        R.drawable.ic_edit,
                        30,
                        Color.parseColor("#FF9502"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Toast.makeText(getContext(), "Update?", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
            }
        };

        loadCartList();
    }

    private void loadCartList() {
        CartDatabase db = CartDatabase.getDbInstance(getContext().getApplicationContext());
        ArrayList<Cart> cartList = (ArrayList<Cart>) db.cartDao().getAllCart();
        if (cartList != null) {
            mCartAdapter.setCartList(cartList);
        }
        mCartSize.setText("(" + cartList.size() + ")");
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getParentFragmentManager().beginTransaction().remove(mCartInformationFragment).commit();
        }
    };

    @Override
    public boolean onBackPressed() {
        if (mCartInformationFragment != null && mCartInformationFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mCartInformationFragment).commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeItem(int cartID) {
        showRemoveDialog(cartID);
    }

    private void showRemoveDialog(int cartID) {
        new AlertDialog.Builder(getContext())
                //.setTitle("Your Cart")
                .setMessage("Remove to Cart?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        CartDatabase db = CartDatabase.getDbInstance(getContext());
                        db.cartDao().deleteFromCart(cartID);
                        loadCartList();
                    }
                }).create().show();
    }
}