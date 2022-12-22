package com.example.nfc_parking1_project.activity;

import android.annotation.SuppressLint;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.CustomerAPI;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.helper.ConvertCardID;
import com.example.nfc_parking1_project.model.Customer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembershipActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    final String TAG = "MembershipActivity";
    private TextView tvLicensePlate;
    private TextView tvTimeVisit;
    private NfcAdapter nfcAdapter;
    private TextView tvPointReward;
    private String token;
    private Button btnResetRewward;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        try {
            token = getIntent().getStringExtra("token");
            Log.d(TAG, getIntent().getStringExtra("token"));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        if (nfcAdapter == null) {
            Toast.makeText(this, "NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            /*finish();*/
        }
        btnResetRewward = findViewById(R.id.btn_reset);
        tvLicensePlate = findViewById(R.id.tv_plateId_result);
        tvTimeVisit = findViewById(R.id.tv_time_visit);
        tvPointReward = findViewById(R.id.tv_point);
        btnResetRewward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerId = tvLicensePlate.getText().toString();
                if(!customerId.equals(""))
                {
                    resetPointReward(customerId);
                }else
                {
                    Toast.makeText(getApplicationContext(), "Customer ID is not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A,
                    null);
        }

    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }

    }

    @Override
    public void onTagDiscovered(Tag tag) {
        String cardId = ConvertCardID.bytesToHex(tag.getId());
        // Check that it is an Ndef capable card
        if (cardId != null) {

            // Catch errors
            try {
                // Success if got to here
                runOnUiThread(() -> {
                    getMemberShip(cardId);
                });

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            // if the NDEF Message to write is malformed


        }
    }

    private void getMemberShip(String cardId) {
        CustomerAPI.customerApi.getCustomer(token, cardId).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (response.code() == 200) {
                        Customer customer = response.body();
                        Log.d(TAG, customer.toString());
                        tvTimeVisit.setText(String.format("Time visits: %s times", String.valueOf(customer.getTimeVisit())));
                        tvLicensePlate.setText(customer.getLicenseNumber());
                        tvPointReward.setText(String.format("Points reward: %d", customer.getPoint()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
    private void resetPointReward(String id){
        CustomerAPI.customerApi.resetPointReward(token,id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try{
                    if(response.code()==200) {
                        MessageResponse messageResponse = response.body();
                        if (messageResponse.getSuccess()) {
                            tvPointReward.setText("Points reward: 0");
                            Toast.makeText(getApplicationContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Toast.makeText(getApplicationContext(),messageResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e)
                {
                    Log.d(TAG,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }
}