package com.example.foodapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cart {
    @PrimaryKey(autoGenerate = true)
    public int cart_id;

    @ColumnInfo(name = "item_title")
    public String itemTitle;

    @ColumnInfo(name = "item_price")
    public String itemPrice;

    @ColumnInfo(name = "no_of_items")
    public int noOfItems;

    @ColumnInfo(name = "item_image")
    public String itemImage;
}
