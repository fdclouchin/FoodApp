package com.example.foodapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodapp.Model.Cart;

import java.util.List;

@Dao
public interface CartDao {
    //sort by total price per item
    @Query("SELECT * FROM cart ORDER BY Cart.item_price * Cart.no_of_items DESC")
    List<Cart> getAllCart();

    @Insert
    void addToCart(Cart... cart);

    @Query("SELECT EXISTS(SELECT * FROM cart WHERE item_title = :itemTitle)")
    boolean isRowIsExist(String itemTitle);

    @Query("SELECT * FROM cart WHERE item_title = :itemTitle")
    List<Cart> retrieveExistingItem(String itemTitle);

    @Query("DELETE FROM cart WHERE `cart_id`=:id")
    void deleteFromCart(int id);
    //delete all from cart
    @Query("DELETE FROM cart")
    void deleteAllItems();
    //update cart noOfItems based on cartID
    @Query("UPDATE cart set no_of_items= :noOfItems WHERE cart_id=:cartID ")
    void updateData(int noOfItems, int cartID);

    //update cart noOfItems based on cartID and itemTitle
    @Query("UPDATE cart set no_of_items= :noOfItems WHERE cart_id=:cartID and item_title=:itemTitle ")
    void updateExistingItem(int noOfItems, int cartID, String itemTitle);
}
