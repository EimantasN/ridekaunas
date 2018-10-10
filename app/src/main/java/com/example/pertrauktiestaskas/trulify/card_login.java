package com.example.pertrauktiestaskas.trulify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class card_login extends AppCompatActivity {

    TextView Status;
    TextView InfoText;
    ImageView Image;
    ProgressBar Laoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_login);

        Status = findViewById(R.id.statustext);
        InfoText = findViewById(R.id.infoText);
        Image = findViewById(R.id.Image);
        Laoding = findViewById(R.id.loading);
    }
}