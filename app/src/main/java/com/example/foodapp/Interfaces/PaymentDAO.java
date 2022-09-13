package com.example.foodapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodapp.Model.Cart;
import com.example.foodapp.Model.PaymentHistory;

import java.util.List;

@Dao
public interface PaymentDAO {

    @Query("SELECT * FROM paymenthistory ORDER BY PaymentHistory.payment_date DESC")
    List<PaymentHistory> showHistory();

    @Insert
    void addToHistory(PaymentHistory... paymentHistory);

}
