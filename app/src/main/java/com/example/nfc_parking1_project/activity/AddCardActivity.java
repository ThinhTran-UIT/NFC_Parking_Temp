package com.example.nfc_parking1_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.helper.ConvertCardID;

import java.io.IOException;

public class AddCardActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private Button buttonExit;
    private Button buttonConfirm;
    private IntentFilter[] writeTagFilters;
    private PendingIntent pendingIntent;
    private TextView idCard;
    private NfcAdapter nfcAdapter;
    Tag NfcTag;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        idCard = (TextView) findViewById(R.id.tv_cardId_result);
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
        }
        else {
            nfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A,
                    null);
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(),PendingIntent.FLAG_IMMUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};
        onNewIntent(getIntent());
        //Start button Confirm
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Ndef mNdef = Ndef.get(tag);
        String cardId = ConvertCardID.bytesToHex(tag.getId());
        // Check that it is an Ndef capable card
        if (mNdef!= null) {
            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.
            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
            // Or if we want to write a Ndef message

            // Create a Ndef Record
            NdefRecord mRecord = NdefRecord.createTextRecord("en","SHOP 1");

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
            } catch (IOException e){
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
}