package com.example.nfc_parking1_project.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.api.UserAPI;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.helper.Helper;
import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.History;
import com.example.nfc_parking1_project.model.User;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    Dialog changePasswordDialog;
    private Context mContext;
    private List<User> mListUser;
    private List<User> mListUserOld;
    public UserAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        final UserAdapter.UserViewHolder userViewHolder = new UserAdapter.UserViewHolder(view);

        return userViewHolder;
    }

    @SuppressLint("Data set changed")
    public void setData(List<User> list) {
        this.mListUser = list;
        this.mListUserOld = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {

            return;
        }
        if(!Constant.CURRENT_ROLE.isEmpty()){
            if(Constant.CURRENT_ROLE.equals(Constant.ROLE_STAFF)){
                holder.btnChangePassword.setVisibility(View.INVISIBLE);
            }
        }
        if(user.getRole().equals(Constant.ROLE_STAFF))
        {
            holder.tvRole.setText("STAFF");
        }
        else {
            holder.tvRole.setText(Constant.ROLE_AMIN);
        }
        holder.tvUsername.setText(user.getName());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());
//        holder.tvShopName.setText(user.getShopName());
        holder.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDialog(user.getPhoneNumber());
                changePasswordDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();

                if(search.isEmpty())
                {
                    mListUser = mListUserOld;
                }else
                {
                    List<User> list = new ArrayList<>();
                    for(User user:mListUserOld)
                    {
                        if(Helper.isCustomerNameSearch(search))
                        {
                            if(user.getName().toLowerCase().contains(search.toLowerCase())){
                                list.add(user);
                            }
                        }
                        else if(Helper.isPhoneNumberSearch(search))
                        {
                            if(user.getPhoneNumber().toLowerCase().contains(search.toLowerCase()))
                            {
                                list.add(user);
                            }
                        }

                    }
                    mListUser = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListUser;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListUser = (List<User>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvPhoneNumber;
        private TextView tvRole;
        private Button btnChangePassword;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvRole = itemView.findViewById(R.id.tv_role);
            btnChangePassword = itemView.findViewById(R.id.btn_edit);
        }
    }
    private void setUpDialog(String userPhoneNumber){
        changePasswordDialog = new Dialog(mContext);
        changePasswordDialog.setContentView(R.layout.dialog_reset_staff_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvNewPassword = changePasswordDialog.findViewById(R.id.edt_new_password);
        TextView tvConfirmPassword = changePasswordDialog.findViewById(R.id.edt_confirm_password);
        Button btnConfirmReset = changePasswordDialog.findViewById(R.id.btn_confirm);
        btnConfirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = tvNewPassword.getText().toString();
                String confirmPassword = tvConfirmPassword.getText().toString();
                Log.d("ResetPasswordDialog","New password"+newPassword);
                Log.d("ResetPasswordDialog","New password"+confirmPassword);
                if(!Helper.validationPassword(newPassword))
                {
                    Toast.makeText(mContext, "Invalid password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!newPassword.equals(confirmPassword))
                    {
                        Toast.makeText(mContext, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Auth auth = new Auth();
                        auth.setPhoneNumber(userPhoneNumber);
                        auth.setNewPassword(tvNewPassword.getText().toString());
                        Log.d("ResetPasswordDialog",auth.toString());
                        resetStaffPassword(auth);
                    }
                }
           
            }
        });
    }
    private void resetStaffPassword(Auth auth)
    {
        try{
            UserAPI.userApi.resetStaffPassword(Constant.TOKEN,auth).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    try
                    {
                        if(response.code()==200)
                        {
                            MessageResponse messageResponse  = response.body();
                            Log.d("ResetPasswordDialog",messageResponse.getMessage());
                            Toast.makeText(mContext, messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            changePasswordDialog.cancel();
                        }
                        else
                        {
                            Toast.makeText(mContext,"Server Error", Toast.LENGTH_SHORT).show();
                            Log.d("ResetPasswordDialog",response.message());
                        }
                    }catch (Exception e)
                    {
                        Log.e("ResetPasswordDialog",e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Log.e("ResetPasswordDialog",t.getMessage());
                }
            });
        }catch (Exception e)
        {
            Log.e("ResetPasswordDialog",e.getMessage());
        }
    }
}
