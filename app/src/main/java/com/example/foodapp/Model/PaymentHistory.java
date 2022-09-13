package com.example.foodapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PaymentHistory {
    @PrimaryKey(autoGenerate = true)
    public int payID;

    @ColumnInfo(name = "payment_id")
    public String paymentID;

    @ColumnInfo(name = "payment_state")
    public String paymentState;

    @ColumnInfo(name = "payment_date")
    public String paymentDate;

}
