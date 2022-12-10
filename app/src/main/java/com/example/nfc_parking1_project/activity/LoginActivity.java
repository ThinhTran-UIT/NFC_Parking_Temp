package com.example.nfc_parking1_project.activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.AuthAPI;
import com.example.nfc_parking1_project.api.AuthRespone;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101010;
    private Button buttonLogin;
    private TextView tvPhoneNumber;
    private TextView tvPassword;
    private Context mContext;
    Button btnFingerPrint;
    private final String TAG = "Login Activity";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_login);


        tvPhoneNumber = (TextView) findViewById(R.id.edt_phone_number);
        tvPassword = (TextView) findViewById(R.id.edt_password);
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
//                    callApiLogin(auth);
                } catch (Exception e) {
                    Log.d("Login Activity", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void callApiLogin(Auth auth) {

        AuthAPI.authApi.login(auth).enqueue(new Callback<AuthRespone>() {
            @Override
            public void onResponse(Call<AuthRespone> call, Response<AuthRespone> response) {
                if(response.code()==200) {
                    AuthRespone authRespone = response.body();
                    if (authRespone.isSuccess()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token",authRespone.getToken());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }else {
                    runOnUiThread(()->{
                        Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<AuthRespone> call, Throwable t) {

            }
        });

    }

}