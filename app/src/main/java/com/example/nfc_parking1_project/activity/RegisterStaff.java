package com.example.nfc_parking1_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private TextView tvStaffName;
    private TextView tvPhoneNumber;
    private TextView tvPassword;
    private TextView tvConfirmPassword;
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
                        Toast.makeText(getApplicationContext(), "Password not match! ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!Helper.validationPhoneNumber(phoneNumber)) {
                        Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    } else if (!Helper.validationPassword(password)) {
                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void callApiCreateStaff(User user) {
        UserAPI.userApi.createStaff(token, user).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 201) {
                    MessageResponse messageResponse = response.body();
                    Toast.makeText(getApplicationContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterStaff.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterStaff.this, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}