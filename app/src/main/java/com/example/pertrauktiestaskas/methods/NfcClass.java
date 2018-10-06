package com.example.pertrauktiestaskas.methods;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class NfcClass extends AppCompatActivity {
    private String serialId = "";
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};


    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            try {
                byte[] tagId = tag.getId();
                serialId = bytesToHex(tagId);
                Log.d("[ReadCardTools]", "Serial Number: " + serialId);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        } else {
            Toast.makeText(this, "This tag is not supported. Action: " + action, Toast.LENGTH_LONG).show();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
