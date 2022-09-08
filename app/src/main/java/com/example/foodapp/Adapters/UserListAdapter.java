package com.example.foodapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.User;
import com.example.foodapp.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.VH> {
    private ArrayList<User> mUsers;
    private Context mContext;

    public UserListAdapter (Context context) {
        this.mContext = context;
    }
    public void setUserList (ArrayList<User> userList) {
        this.mUsers = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_layout, parent, false);
        return new UserListAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.VH holder, int position) {
        holder.mFirstName.setText(this.mUsers.get(position).firstName);
        holder.mLastName.setText(this.mUsers.get(position).lastName);
        holder.mNumber.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return (this.mUsers == null) ? 0: this.mUsers.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private TextView mFirstName;
        private TextView mLastName;
        private TextView mNumber;
        private ConstraintLayout mItem;

        public VH(@NonNull View itemView) {
            super(itemView);
            mFirstName = itemView.findViewById(R.id.first_name_list);
            mLastName = itemView.findViewById(R.id.last_name_list);
            mNumber = itemView.findViewById(R.id.list_position);
            mItem = itemView.findViewById(R.id.item_layout);

            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
