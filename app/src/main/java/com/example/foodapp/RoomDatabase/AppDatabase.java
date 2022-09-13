package com.example.foodapp.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodapp.Interfaces.CartDao;
import com.example.foodapp.Interfaces.UserDao;
import com.example.foodapp.Model.Cart;
import com.example.foodapp.Model.User;

@Database(entities = {User.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
