package com.example.nfc_parking1_project.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.CardAPI;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.helper.ConvertCardID;
import com.example.nfc_parking1_project.kotlin.ScanActivityKotlin;
import com.example.nfc_parking1_project.model.Card;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.GsonBuilder;

import org.opencv.android.OpenCVLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String ERROR_DETECTED = "No NFC Detected";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    public static MainActivity instance = null;
    BottomNavigationView bottomNavigationView;
    PendingIntent pendingIntent;
    Tag NfcTag;

    private String cardId;
    private Intent intentScan;
    private Intent intentOut;
    private NfcAdapter nfcAdapter = null;
    private String TAG = "MainActivity";
    private IntentFilter[] writeTagFilters;
    private Dialog addCardDialog;
    private Dialog restoreCardDialog;
    private TextView tvCardIdDialog;
    private Button btnAddCardDiaglog;
    private TextView tvCardStatusDialog;
    private Button btnExitAddCardDialog;
    private PendingIntent mPendingIntent;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Button btnRecovery;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set up navigation


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.
                Builder(R.id.navigation_card, R.id.navigation_history, R.id.navigation_staff, R.id.navigation_profile)
                .build();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        ContextCompat.getMainExecutor(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //Set up diaglog add card
        setUpDialogAddCard();
        //Set up dialog restore card
        setUpDialogRestoreCard();

        // Check opencv
        Log.d("OPENCV", "Loading OPENCV status" + OpenCVLoader.initDebug());
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            /*finish();*/
        } else {
//            nfcAdapter.enableReaderMode(this,
//                    this,
//                    NfcAdapter.FLAG_READER_NFC_A ,
//                    null);
            Log.d(TAG, "On Create enable Reader");
        }


        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                , PendingIntent.FLAG_MUTABLE);
//        pendingIntent.cancel();
        onNewIntent(getIntent());
        //Bottom Navigation Bar
    }


    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume");
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        if (nfcAdapter != null) {
//            nfcAdapter.enableReaderMode(this,
//                    this,
//                    NfcAdapter.FLAG_READER_NFC_A,
//                    null);
//            Log.d(TAG, "On Create enable Reader");
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String action = intent.getAction();
        try {
            Log.e(TAG, action);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            cardId = ConvertCardID.bytesToHex(tag.getId());
            Log.d(TAG, cardId);
            callApiGetCard(cardId);
        } else {
            Log.d(TAG, "Cannot detect");
        }
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        Log.d(TAG, "On Pause");
        if (nfcAdapter != null) {
//            nfcAdapter.disableReaderMode(this);
            nfcAdapter.disableForegroundDispatch(this);
            Log.d(TAG, "On Pause Disable Reader");
        } else {
            Log.d(TAG, "On Pause Disable Reader Fail");
        }

    }


    public void callApiGetCard(String cardId) {
        CardAPI.cardApi.getOneCard(Constant.TOKEN, cardId).enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                try {
                    String cardStatus;
                    Log.d(TAG, cardId);
                    if (response.code() == 200) {
                        Card card = response.body();
                        cardStatus = card.getStatus();
                        Log.d(TAG, cardStatus);
                        Bundle bundle = new Bundle();
                        bundle.putString("cardId", cardId);
                        bundle.putString("token", Constant.TOKEN);
                        switch (cardStatus) {
                            case "AVAILABLE":
                                intentScan = new Intent(MainActivity.this, ScanActivityKotlin.class);
                                intentScan.putExtras(bundle);
                                startActivity(intentScan);
                                break;
                            case "IN_USE":
                                intentOut = new Intent(MainActivity.this, Detail_Info_Plate.class);
                                intentOut.putExtras(bundle);
                                startActivity(intentOut);
                                break;
                            case "NEW_CARD":
                                tvCardIdDialog.setText(cardId);
                                addCardDialog.show();
                                break;
                            case "LOST":
                                restoreCardDialog.show();
                                break;
                        }


                    } else {
                        Toast.makeText(MainActivity.this, "Card invalid", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {

            }
        });

    }


//    @Override
//    public void onTagDiscovered(Tag tag) {
//        Ndef mNdef = Ndef.get(tag);
//        String cardId = ConvertCardID.bytesToHex(tag.getId());
//        // Check that it is an Ndef capable card
//        if (cardId != null) {
//            // If we want to read
//            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
//            // We can get the cached Ndef message the system read for us.
//            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
//            // Or if we want to write a Ndef message
//
//            // Create a Ndef Record
//            NdefRecord mRecord = NdefRecord.createTextRecord("en", "SHOP 1");
//
//            // Add to a NdefMessage
//            NdefMessage mMsg = new NdefMessage(mRecord);
//
//            // Catch errors
//            try {
//                mNdef.connect();
//                mNdef.writeNdefMessage(mMsg);
//                // Success if got to here
//                runOnUiThread(() -> {
//                   callApiGetCard(cardId);
//                });
//
//                // Make a Sound
//                try {
//                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
//                            notification);
//                    r.play();
//                } catch (Exception e) {
//                    // Some error playing sound
//                }
//
//            } catch (FormatException e) {
//                // if the NDEF Message to write is malformed
//            } catch (TagLostException e) {
//                // Tag went out of range before operations were complete
//            } catch (IOException e) {
//                // if there is an I/O failure, or the operation is cancelled
//            } finally {
//                // Be nice and try and close the tag to
//                // Disable I/O operations to the tag from this TagTechnology object, and release resources.
//                try {
//                    mNdef.close();
//                } catch (IOException e) {
//                    // if there is an I/O failure, or the operation is cancelled
//                }
//            }
//
//        }
//
//    }

    private void setUpDialogAddCard() {
        addCardDialog = new Dialog(this);
        addCardDialog.setContentView(R.layout.dialog_add_card);
        addCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvCardIdDialog = addCardDialog.findViewById(R.id.tv_cardId_result);
        tvCardStatusDialog = addCardDialog.findViewById(R.id.tv_card_status);
        btnAddCardDiaglog = addCardDialog.findViewById(R.id.btn_confirm);
        btnAddCardDiaglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = new Card();
                card.setId(tvCardIdDialog.getText().toString());
                addCard(card);

            }
        });
        btnExitAddCardDialog = addCardDialog.findViewById(R.id.btn_exit);
        btnExitAddCardDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardDialog.cancel();
            }
        });
    }

    private void setUpDialogRestoreCard() {
        restoreCardDialog = new Dialog(this);
        restoreCardDialog.setContentView(R.layout.dialog_restore_card);
        restoreCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnRecovery = restoreCardDialog.findViewById(R.id.btn_recovery);
        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoveryCard(cardId);

            }
        });
    }

    private void recoveryCard(String cardId) {
        CardAPI.cardApi.recoveryCard(Constant.TOKEN, cardId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    if (response.code() == 200) {
                        MessageResponse messageResponse = response.body();
                        if (messageResponse.getSuccess()) {
                            Toast.makeText(getApplicationContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            restoreCardDialog.cancel();
                        } else {
                            Toast.makeText(MainActivity.this, messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, response.message());
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, response.message());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void addCard(Card card) {
        CardAPI.cardApi.createCard(Constant.TOKEN, card).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    MessageResponse messageResponse = response.body();
                    if (messageResponse.getSuccess()) {
                        tvCardStatusDialog.setText("Create card successfully!");
                        int cardStatusColor = ResourcesCompat.getColor(getApplicationContext().getResources(), R.color.green, null);
                        tvCardStatusDialog.setTextColor(cardStatusColor);
                        Toast.makeText(getApplicationContext(), "Create Card successfully!", Toast.LENGTH_SHORT).show();
                        addCardDialog.cancel();
                    }
                } else {
                    tvCardStatusDialog.setText("Card is exist!");
                    int cardStatusColor = ResourcesCompat.getColor(getApplicationContext().getResources(), R.color.red, null);
                    tvCardStatusDialog.setTextColor(cardStatusColor);
                }
                Log.w("Add Card Activity", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                tvCardStatusDialog.setText(response.message());

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }
}