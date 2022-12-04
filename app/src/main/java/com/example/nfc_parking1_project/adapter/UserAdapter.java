package com.example.nfc_parking1_project.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.activity.RegisterStaff;
import com.example.nfc_parking1_project.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> mListUser;
    MainActivity mainActivity;
    Dialog changePasswordDialog;
    public UserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        final UserAdapter.UserViewHolder userViewHolder = new UserAdapter.UserViewHolder(view);

        changePasswordDialog = new Dialog(mContext);
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView tvCardId = (TextView) editProfileDialog.findViewById(R.id.tv_card_id);

                changePasswordDialog.show();
            }
        });
        return userViewHolder;
    }

    @SuppressLint("Data set changed")
    public void setData(List<User> list) {
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {

            return;
        }
        holder.tvUsername.setText(user.getUsername());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());
        holder.tvShopName.setText(user.getShopName());
    }

    @Override
    public int getItemCount() {
        if (mListUser != null){
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvPhoneNumber;
        private TextView tvShopName;
        private Button btnEdit;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);

//            btnEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }


    }
}
