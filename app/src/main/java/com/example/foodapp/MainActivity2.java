package com.example.foodapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodapp.Adapters.UserListAdapter;
import com.example.foodapp.Model.User;
import com.example.foodapp.RoomDatabase.AppDatabase;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button addNewUser = findViewById(R.id.add_new_user_button);
        Button intro = findViewById(R.id.intro_button);
        intro.setOnClickListener(mOnClickListener);
        addNewUser.setOnClickListener(mOnClickListener);

        initRecyclerView();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.add_new_user_button: {
                    Intent intent = new Intent(MainActivity2.this, RoomActivity.class);
                    someActivityResultLauncher.launch(intent);
                    break;
                }
                case R.id.intro_button: {
                    startActivity(new Intent(MainActivity2.this, IntroActivity.class));
                    finish();
                    break;
                }
            }
        }
    };

    private void initRecyclerView() {
        RecyclerView mUserRecyclerList = findViewById(R.id.user_list);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mUserRecyclerList.addItemDecoration(dividerItemDecoration);

        userListAdapter = new UserListAdapter(this);
        mUserRecyclerList.setAdapter(userListAdapter);
        loadUserList();
    }

    private void loadUserList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        ArrayList<User> userList = (ArrayList<User>) db.userDao().getAllUsers();
        if (userList != null) {
            userListAdapter.setUserList(userList);
        }
    }
    
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            loadUserList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        loadUserList();
                    }
                }
            });
}