package com.example.nfc_parking1_project.activity;


import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.nfc_parking1_project.api.AuthAPI;
import com.example.nfc_parking1_project.api.AuthRespone;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101010;
    private final String TAG = "Login Activity";
    private Button buttonLogin;
    private EditText tvPhoneNumber;
    private EditText tvPassword;
    private TextView tvNotifyLogin;
    private Context mContext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);
        tvNotifyLogin = findViewById(R.id.tv_login_notify);
        tvPhoneNumber =  findViewById(R.id.edt_phone_number);
        tvPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotifyLogin.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvPassword =  findViewById(R.id.edt_password);
        tvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNotifyLogin.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Start button redirect scan activity
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject authObject = new JSONObject();
                try {
                    String phoneNumber = tvPhoneNumber.getText().toString();
                    String password = tvPassword.getText().toString();
                    Auth auth = new Auth(phoneNumber, password);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
                    callApiLogin(auth);
                } catch (Exception e) {
                    Log.d("Login Activity", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void callApiLogin(Auth auth) {
        Log.d("Login Activity", "api work");

        AuthAPI.authApi.login(auth).enqueue(new Callback<AuthRespone>() {
            @Override
            public void onResponse(Call<AuthRespone> call, Response<AuthRespone> response) {
                Log.d("Login Activity", String.valueOf(response.code()));
                if (response.code() == 200) {
                    AuthRespone authRespone = response.body();
                    if (authRespone.isSuccess()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        User user = authRespone.getUser();
                        Constant.CURRENT_ROLE=user.getRole();
                        Constant.TOKEN = "Bearer "+ authRespone.getToken();
                       startActivity(intent);
                        finish();
                    }
                    else{

                    }
                } else {
                    tvNotifyLogin.setText("Incorrect phone number or password!");
                   Log.e(TAG,response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthRespone> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });

    }

}