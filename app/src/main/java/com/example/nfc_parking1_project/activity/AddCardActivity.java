package com.example.nfc_parking1_project.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
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
import com.example.nfc_parking1_project.helper.ConvertCardID;
import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.model.History;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    Tag NfcTag;
    Context context = AddCardActivity.this;
    private Button buttonExit;
    private Button buttonConfirm;
    private TextView cardStatus;
    private TextView idCard;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        idCard = (TextView) findViewById(R.id.tv_cardId_result);
        cardStatus = (TextView) findViewById(R.id.tv_card_status);
        //Start button Exit
        buttonExit = (Button) findViewById(R.id.btn_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            nfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A,
                    null);
        }

        //Start button Confirm
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card = new Card();
                card.setId(idCard.getText().toString());
                callApiCreateCard(card);
                Toast.makeText(AddCardActivity.this, "call api ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callApiCreateCard(Card card) {
//        CardAPI.cardApi.createCard(token,card).enqueue(new Callback<MessageResponse>() {
//            @Override
//            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
//                if(response.code()==200)
//                {
//                    MessageResponse messageResponse = response.body();
//                    if(messageResponse.getSuccess())
//                    {
//                        cardStatus.setText("Create card successfully!");
//                        int cardStatusColor = ResourcesCompat.getColor(getApplicationContext().getResources(),R.color.green,null);
//                        cardStatus.setTextColor(cardStatusColor);
//                    }
//                }
//                else {
//                    cardStatus.setText("Card is exist!");
//                    int cardStatusColor = ResourcesCompat.getColor(getApplicationContext().getResources(),R.color.red,null);
//                    cardStatus.setTextColor(cardStatusColor);
//                }
//                Log.w("Add Card Activity",new GsonBuilder().setPrettyPrinting().create().toJson(response));
//                cardStatus.setText(response.message());
//
//            }
//            @Override
//            public void onFailure(Call<MessageResponse> call, Throwable t) {
//
//            }
//        });
    }


    @Override
    public void onTagDiscovered(Tag tag) {
        Ndef mNdef = Ndef.get(tag);
        String cardId = ConvertCardID.bytesToHex(tag.getId());
        // Check that it is an Ndef capable card
        if (mNdef != null) {
            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.
            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
            // Or if we want to write a Ndef message

            // Create a Ndef Record
            NdefRecord mRecord = NdefRecord.createTextRecord("en", "SHOP 1");

            // Add to a NdefMessage
            NdefMessage mMsg = new NdefMessage(mRecord);

            // Catch errors
            try {
                mNdef.connect();
                mNdef.writeNdefMessage(mMsg);
                // Success if got to here
                runOnUiThread(() -> {
                    idCard.setText(cardId);
                });

                // Make a Sound
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                            notification);
                    r.play();
                } catch (Exception e) {
                    // Some error playing sound
                }

            } catch (FormatException e) {
                // if the NDEF Message to write is malformed
            } catch (TagLostException e) {
                // Tag went out of range before operations were complete
            } catch (IOException e) {
                // if there is an I/O failure, or the operation is cancelled
            } finally {
                // Be nice and try and close the tag to
                // Disable I/O operations to the tag from this TagTechnology object, and release resources.
                try {
                    mNdef.close();
                } catch (IOException e) {
                    // if there is an I/O failure, or the operation is cancelled
                }
            }

        }

    }

    public void callApiGetCard(String cardId) {
//        CardAPI.cardApi.getOneCard(cardId).enqueue(new Callback<Card>() {
//            @Override
//            public void onResponse(Call<Card> call, Response<Card> response) {
//                try {
//                    String cardStatus;
//                    if (response.code() == 200) {
//                        Card card = response.body();
//                        cardStatus = card.getStatus();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("cardId", cardId);
//                        if (cardStatus.equals("AVAILABLE")) {
//                            Intent intentScan = new Intent(AddCardActivity.this, ScanActivityKotlin.class);
//                            intentScan.putExtras(bundle);
//                            startActivity(intentScan);
//                        } else if (cardStatus.equals("IN_USE")) {
//                            Intent intentOut= new Intent(AddCardActivity.this, Detail_Info_Plate.class);
//                            intentOut.putExtras(bundle);
//                            startActivity(intentOut);
//                        }else
//                        {
//                            Toast.makeText(AddCardActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } else {
//                        Toast.makeText(AddCardActivity.this, "Card invalid", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(AddCardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Card> call, Throwable t) {
//
//            }
//        });

    }


}