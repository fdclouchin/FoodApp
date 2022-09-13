package com.example.foodapp.RoomDatabase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodapp.Interfaces.PaymentDAO;
import com.example.foodapp.Model.PaymentHistory;

@Database(entities = {PaymentHistory.class}, version = 2)

public abstract class PaymentDatabase extends RoomDatabase {

    public abstract PaymentDAO paymentDao();

    private static PaymentDatabase INSTANCE;

    public static PaymentDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PaymentDatabase.class, "Payment_DB")
                    /*.fallbackToDestructiveMigration()*/ //if db version is updated use this to wipe clean the db
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}
