package com.example.nfc_parking1_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.api.UserAPI;
import com.example.nfc_parking1_project.helper.Helper;
import com.example.nfc_parking1_project.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStaff extends AppCompatActivity {

    public static final String TAG = "RegisterStaff";
    private Button btnRegister;
    private Button btnHome;
    private EditText tvStaffName;
    private EditText tvPhoneNumber;
    private EditText tvPassword;
    private EditText tvConfirmPassword;
    private  TextView tvNotify;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);
        //Get authorize token

        try {
            token = getIntent().getStringExtra("token");
            Log.d(TAG, getIntent().getStringExtra("token"));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        tvNotify = findViewById(R.id.tv_notify_register);
        tvStaffName = findViewById(R.id.edt_full_name);
        tvPhoneNumber = findViewById(R.id.edt_phone_number);
        tvPassword = findViewById(R.id.edt_password);
        tvConfirmPassword = findViewById(R.id.edt_confirm_password);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvStaffName.getText().toString();
                String phoneNumber = tvPhoneNumber.getText().toString();
                String password = tvPassword.getText().toString();
                String confirmPassword = tvConfirmPassword.getText().toString();
                Log.d(TAG, password);
                if (Helper.validationPhoneNumber(phoneNumber) && Helper.validationPassword(password)) {
                    if (confirmPassword.equals(password)) {
                        User user = new User(name, phoneNumber, password);
                        callApiCreateStaff(user);
                    } else {
                        tvNotify.setText("Password doest not match!");
                    }
                } else {
                    if (!Helper.validationPhoneNumber(phoneNumber)) {
                        tvNotify.setText("Invalid phone number");
                    } else if (!Helper.validationPassword(password)) {
                        tvNotify.setText("Invalid password");
                    }

                }
            }
        });

        btnHome= findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Set up destroy error message
        setUpDestroyErrorMessage();
    }
    private void setUpDestroyErrorMessage(){
        tvPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotify.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvStaffName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotify.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotify.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotify.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void callApiCreateStaff(User user) {
        Log.d(TAG,user.toString());
        UserAPI.userApi.createStaff(token, user).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    MessageResponse messageResponse = response.body();
                    if(messageResponse.getSuccess())
                    {
                        tvNotify.setText(messageResponse.getMessage());
                    }else {
                        tvNotify.setText(messageResponse.getMessage());
                    }

                } else {
                    tvNotify.setText(response.message());
                }

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}