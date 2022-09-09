package com.example.foodapp.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.foodapp.Adapters.UserListAdapter;
import com.example.foodapp.Helper.SwipeHelper;
import com.example.foodapp.Interfaces.ButtonClickListener;
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.example.foodapp.RoomDatabase.AppDatabase;

import java.util.ArrayList;
import java.util.List;

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
            switch (v.getId()) {
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

        /*SwipeHelper swipeHelper = new SwipeHelper(this, mUserRecyclerList, 200) {
            @Override
            public void instantiateItemButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.ItemButton> buffer) {
                buffer.add(new ItemButton(
                        viewHolder.getAdapterPosition(),
                        MainActivity2.this,
                        "DELETE",
                        R.drawable.ic_delete,
                        30,
                        Color.parseColor("#FF3C30"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Toast.makeText(MainActivity2.this, "Delete?", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
                buffer.add(new ItemButton(
                        viewHolder.getAdapterPosition(),
                        MainActivity2.this,
                        "EDIT",
                        R.drawable.ic_edit,
                        30,
                        Color.parseColor("#FF9502"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Toast.makeText(MainActivity2.this, "EDIT?", Toast.LENGTH_SHORT).show();
                            }
                        }
                ));
            }
        };*/

        loadUserList();
    }

    private void loadUserList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        ArrayList<User> userList = (ArrayList<User>) db.userDao().getAllUsers();
        if (userList != null) {
            userListAdapter.setUserList(userList);
        }
    }

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