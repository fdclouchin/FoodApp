package com.example.foodapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.foodapp.Helper.PaypalConfig;
import com.example.foodapp.Model.PaymentHistory;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.PaymentDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaypalCheckOutActivity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String PAYMENT_TOTAL = "PAYMENT_TOTAL";
    // Paypal Configuration Object
    private static PayPalConfiguration mPayPaylConfiguration = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX) or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            // on below line we are passing a client id.
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    private TextView mPaymentStatus;
    private PaymentDatabase mPaymentDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_check_out);
        mPaymentStatus = findViewById(R.id.payment_status);
        mPaymentDB = PaymentDatabase.getDbInstance(this);
        getPayment();
    }

    private void getPayment() {
        Bundle extras = getIntent().getExtras();
        // Getting the amount from editText
        String amount = extras.getString(PAYMENT_TOTAL);
        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Course Fees",
                PayPalPayment.PAYMENT_INTENT_SALE);
        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPaylConfiguration);
        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        mPaymentStatus.setText("Payment " + state + "\n with payment id is " + payID);


                        addToHistory(payID, state);
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void addToHistory(String payID, String state) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String paymentDate = formatter.format(date);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.paymentID = payID;
        paymentHistory.paymentState = state;
        paymentHistory.paymentDate = paymentDate;
        mPaymentDB.paymentDao().addToHistory(paymentHistory);
        Log.d("addToHistory", "PaymentID " + payID + "\n PaymentSTATE " + state + "\n PaymentDate " + paymentDate);
    }
}