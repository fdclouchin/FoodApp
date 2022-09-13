package com.example.foodapp.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodapp.Interfaces.CartDao;
import com.example.foodapp.Interfaces.UserDao;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.Model.User;

@Database(entities = {Cart.class}, version = 2)

public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDao cartDao();

    private static CartDatabase INSTANCE;

    public static CartDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CartDatabase.class, "Cart_DB")
                    /*.fallbackToDestructiveMigration()*/ //if db version is updated use this to wipe clean the db
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
