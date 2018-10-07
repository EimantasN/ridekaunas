package com.example.pertrauktiestaskas.trulify;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pertrauktiestaskas.methods.BusApiHandler;
import com.example.pertrauktiestaskas.models.RootObject;
import com.example.pertrauktiestaskas.models.TrafiListModel;

import java.util.List;

public class Register extends AppCompatActivity {

    private String serialId = "";
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    NfcAdapter adapter;

    BusApiHandler handler;
    Button AddCard;

    ProgressBar progress;

    TextView CardName;
    TextView CartLastName;

    TextView header;

    TextView IDText;
    TextView ValidTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Register");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_layout);

        header = findViewById(R.id.textView5);

        AddCard = (Button) findViewById(R.id.button4);
        AddCard.setEnabled(false);
        AddCard.setBackgroundColor(Color.GRAY);

        AddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCard();
            }
        });

        handler = new BusApiHandler();

        adapter = NfcAdapter.getDefaultAdapter(this);
        onNewIntent(this.getIntent());


        CardName = findViewById(R.id.textView11);
        CartLastName = findViewById(R.id.textView10);

        IDText = findViewById(R.id.textView13);
        ValidTime = findViewById(R.id.textView9);

        progress = findViewById(R.id.progressBar2);


        TextInputEditText name = findViewById(R.id.nameText);
        TextInputEditText lastName = findViewById(R.id.lastnameText);

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    CardName.setText(s.toString());
                else
                    CardName.setText("vardenis");
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    CartLastName.setText(s.toString());
                else
                    CartLastName.setText("pavardenis");
            }
        });

    }

    public void AddCard()
    {
        Intent i = new Intent(getApplicationContext(), register_account.class);
        i.putExtra("studentId", "1");
        startActivity(i);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        PendingIntent pendingIntent     = PendingIntent.getActivity(this,0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter[] intentFilters    = { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };

        adapter.enableForegroundDispatch(   this,
                pendingIntent,
                intentFilters,
                new String[][]{
                        new String[]{"android.nfc.tech.NfcA"}
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (adapter != null)
        {
            try {
                adapter.disableForegroundDispatch(this);
            }
            catch (NullPointerException e) {
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        //Toast.makeText(this,""+intent.getAction(), Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equalsIgnoreCase(action)) {
            try {
                byte[] tagId = tag.getId();
                serialId = bytesToHex(tagId);

                IDText.setText("0000   0000   00" +
                        serialId.toCharArray()[0] + serialId.toCharArray()[1] +""+
                    "   " +  serialId.toCharArray()[2] +
                        serialId.toCharArray()[3] +
                        serialId.toCharArray()[4] +
                        serialId.toCharArray()[5]);

                ValidTime.setText("Valid unitl 2023-07-01");

                header.setText("Synchronization in progress...");

                progress.setVisibility(progress.VISIBLE);

                new LongOperation().execute();

            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        } else {
            //Toast.makeText(this, "This tag is not supported. Action: " + action, Toast.LENGTH_LONG).show();
        }
    }


private class LongOperation extends AsyncTask<String, Void, String> {

    List<TrafiListModel> data;
    @Override
    protected String doInBackground(String... params) {
        try {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            String vc = e.getMessage();
        }

        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        header.setText("Card added");
        progress.setVisibility(progress.GONE);
        AddCard.setEnabled(true);
        AddCard.setBackgroundColor(Color.parseColor("#88c024"));

    }


    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
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