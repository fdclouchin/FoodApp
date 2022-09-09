package com.example.foodapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodapp.Model.Cart;

import java.util.List;

@Dao
public interface CartDao {
    //sort by total price per item
    @Query("SELECT * FROM cart ORDER BY item_price * no_of_items DESC")
    List<Cart> getAllCart();

    @Insert
    void addToCart(Cart... cart);

    @Query("DELETE FROM cart WHERE `cart_id`=:id")
    void deleteFromCart(int id);

    @Query("UPDATE cart set item_title= :itemTitle, item_price= :itemPrice, no_of_items= :noOfItems, item_image= :itemImage WHERE cart_id=:id ")
    void updateData(String itemTitle, String itemPrice, int noOfItems, String itemImage, int id);
}
