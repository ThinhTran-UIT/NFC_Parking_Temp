package com.example.nfc_parking1_project.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.LoginActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.api.AuthAPI;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.api.UserAPI;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.helper.Helper;
import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    static final String TAG = "Profile Fragment";
    MainActivity mainActivity;
    private Button btnLogout;
    private Button btnAcceptLogout;
    private Button btnEditProfileDialog;
    private Button btnChangePasswordDialog;
    private Dialog editProfileDialog;
    private Dialog changePasswordDialog;
    private Dialog changeProfileSuccessDialog;
    private TextView tvProfileName;
    private TextView tvProfilePhoneNumber;
    private TextView profileRole;
    private TextView tvNewFullName;
    private TextView tvNewPhoneNumber;
    private TextView tvOldPassword;
    private TextView tvNewPassword;
    private TextView tvConfirmPassword;
    private TextView tvNotifySuccess;
    private Button btnConfirmChangePassword;
    private Button btnConfirmEdit;
    private String token;
    private int userId;
    private static User user = new User();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup containers, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, containers, false);

        //Button Logout user(staff)

        btnLogout = view.findViewById(R.id.btn_logout);
        mainActivity = (MainActivity) getActivity();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        //Find layout for button and component
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfilePhoneNumber = view.findViewById(R.id.tv_display_phonenumber);
        profileRole = view.findViewById(R.id.tv_role_name);
        btnEditProfileDialog = view.findViewById(R.id.btn_edit_profile);
        btnChangePasswordDialog = view.findViewById(R.id.btn_change_password);
        //Set up dialog
        setUpDialog();
        btnEditProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNewFullName.setText(tvProfileName.getText());
                tvNewPhoneNumber.setText(tvProfilePhoneNumber.getText());
                editProfileDialog.show();
            }
        });
        btnChangePasswordDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.show();
            }
        });
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            token = bundle.getString("token");
        }
        Log.d(TAG, token);
        if (token != null) {
            callApiGetProfile(token);
        }
        return view;
    }

    private void setUpDialog() {
        // Create edit profile dialog
        editProfileDialog = new Dialog(getContext());
        editProfileDialog.setContentView(R.layout.dialog_edit_profile);
        editProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvNewFullName = editProfileDialog.findViewById(R.id.edt_full_name);
        tvNewPhoneNumber = editProfileDialog.findViewById(R.id.edt_phone_number);
        btnConfirmEdit = editProfileDialog.findViewById(R.id.btn_confirm);
        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName(tvNewFullName.getText().toString());
                user.setPhoneNumber(tvNewPhoneNumber.getText().toString());
                callApiEditProfile(userId, user);
            }
        });
        //Create change password dialog
        changePasswordDialog = new Dialog(getContext());
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvOldPassword = changePasswordDialog.findViewById(R.id.edt_old_password);
        tvNewPassword = changePasswordDialog.findViewById(R.id.edt_new_password);
        tvConfirmPassword = changePasswordDialog.findViewById(R.id.edt_confirm_password);
        btnConfirmChangePassword = changePasswordDialog.findViewById(R.id.btn_confirm);
        btnConfirmChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = tvOldPassword.getText().toString();
                String newPassword = tvNewPassword.getText().toString();
                String confirmPassword = tvConfirmPassword.getText().toString();
                String phoneNumber = tvProfilePhoneNumber.getText().toString();
                if (Helper.validationPassword(newPassword)) {
                    if (newPassword.equals(confirmPassword)) {
                        Auth auth = new Auth();
                        auth.setPhoneNumber(phoneNumber);
                        auth.setPassword(oldPassword);
                        auth.setNewPassword(confirmPassword);
                        changePassword(auth);
                    } else {
                        Toast.makeText(getContext(), "Password doest not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Password is not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        changeProfileSuccessDialog = new Dialog(getContext());
        changeProfileSuccessDialog.setContentView(R.layout.dialog_notify_logout);
        changeProfileSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnAcceptLogout = changeProfileSuccessDialog.findViewById(R.id.btn_logout);
        tvNotifySuccess = changeProfileSuccessDialog.findViewById(R.id.tv_notify_success);
        btnAcceptLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }


    private void callApiGetProfile(String token) {
        Log.d(TAG, "get profile");
        AuthAPI.authApi.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.code() == 200) {
                        user = response.body();
                        Log.d(TAG, user.toString());
                        userId = user.getId();
                        tvProfileName.setText(user.getName());
                        tvProfilePhoneNumber.setText(user.getPhoneNumber());
                        if(user.getRole().equals(Constant.ROLE_STAFF)){
                            profileRole.setText("STAFF");
                        }
                        else {
                            profileRole.setText(Constant.ROLE_AMIN);
                        }

                    } else {
                        Toast.makeText(getContext(), "Can not get user profile", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, response.message());
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


    public void callApiEditProfile(int id, User user) {
        UserAPI.userApi.updateUserInfo(token, id, user).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    MessageResponse messageResponse;
                    if (response.code() == 200) {
                        messageResponse = response.body();
                        Toast.makeText(getContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        editProfileDialog.cancel();
                        tvNotifySuccess.setText("Update Profile Infomation Successfully. Please login again!");
                        changeProfileSuccessDialog.show();
                    } else if (response.code() == 409) {
                        Log.d(TAG, response.message());
                        Toast.makeText(getContext(), "New phone number is exist in the system!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, response.message());
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword(Auth request) {
        UserAPI.userApi.changePassword(token, request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        MessageResponse messageResponse = response.body();
                        Toast.makeText(getContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        changePasswordDialog.cancel();
                        tvNotifySuccess.setText("Change password successfully. Please login again!");
                        changeProfileSuccessDialog.show();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void logOut() {
        AuthAPI.authApi.logOut(token).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MessageResponse messageResponse = response.body();
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Log out successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    mainActivity.finish();
                } else {

                    Toast.makeText(getContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    mainActivity.finish();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


}
