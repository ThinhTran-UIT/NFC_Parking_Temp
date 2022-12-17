package com.example.nfc_parking1_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.HistoryAPI;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.model.History;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Info_Plate extends AppCompatActivity {
    static final String TAG = "DetailInfoActivity";
    private String cardId;
    private int historyId;
    private Button buttonExit;
    private Button buttonConfirm;
    private TextView tvLicensePlate;
    private TextView tvTimeIn;
    private TextView tvTimeOut;
    private TextView tvCardId;
    private TextView tvUserIdIn;
    private TextView tvUserIdOut;
    private String token;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_plate);

        try {
            cardId = getIntent().getStringExtra("cardId");
            Log.d(TAG, getIntent().getStringExtra("cardId"));
            token = getIntent().getStringExtra("token");
            Log.d(TAG, token);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        tvLicensePlate = findViewById(R.id.tv_plateId_result);
        tvCardId = findViewById(R.id.tv_card_id);
        tvTimeIn = findViewById(R.id.tv_time_in);
        tvTimeOut = findViewById(R.id.tv_time_out);
        tvUserIdIn = findViewById(R.id.tv_staff_confirm_getin);
        tvUserIdOut = findViewById(R.id.tv_staff_confirm_getout);

        //Start button Exit
        buttonExit = (Button) findViewById(R.id.btn_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Detail_Info_Plate.this, MainActivity.class);
//                intent.putExtra("token",token);
//                startActivity(intent);
                finish();
            }
        });
        getHistory(cardId);

        //Start button Confirm
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmVehicleOut();
            }
        });
    }

    private void getHistory(String cardId) {
        HistoryAPI.historyApi.getHistory(token, cardId).enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                try {
                    if (response.code() == 200) {
                        History history = response.body();
                        historyId = history.getId();
                        Log.d(TAG, history.toString());
                        tvLicensePlate.setText(history.getLicenseNumber());
                        tvCardId.setText(history.getCardId());
                        tvTimeIn.setText(history.getTimeIn());
                        tvTimeOut.setText("N/A");
                        if (!history.getUserCheckin().isEmpty()) {
                            tvUserIdIn.setText(history.getUserCheckin());
                            tvUserIdOut.setText(history.getUserCheckout());
                        } else {
                            tvUserIdIn.setText("N/A");
                            tvUserIdOut.setText("N/A");
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {

            }
        });
    }

    private void confirmVehicleOut() {
        HistoryAPI.historyApi.vehicleOut(token, historyId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    if (response.code() == 200) {
                        MessageResponse messageResponse = response.body();
                        if (messageResponse.getSuccess()) {
                            Toast.makeText(getApplicationContext(), "Customer checkout successfully!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Detail_Info_Plate.this, MainActivity.class);
//                            intent.putExtra("token",token);
//                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}