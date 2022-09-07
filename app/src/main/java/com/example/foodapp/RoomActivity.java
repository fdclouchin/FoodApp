package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RoomActivity extends AppCompatActivity {
    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private Button mSaveButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        
        mFirstNameInput = findViewById(R.id.first_name);
        mLastNameInput = findViewById(R.id.last_name);
        mSaveButton = findViewById(R.id.save_button);
        
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewUser(mFirstNameInput.getText().toString(), mLastNameInput.getText().toString());
            }
        });
    }

    private void saveNewUser(String firstName, String lastName) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        db.userDao().insertUser();

    }
}