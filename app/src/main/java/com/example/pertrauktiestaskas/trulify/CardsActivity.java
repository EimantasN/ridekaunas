package com.example.pertrauktiestaskas.trulify;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardsActivity extends AppCompatActivity {
    TextView RemainingMoney;
    TextView LastUsed;
    TextView ValidUntil;
    Button SyncButton;
    Button AddButton;
    Button BlockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("All Cards");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

//        SyncButton = (Button) findViewById(R.id.sync_button);
//        AddButton = (Button) findViewById(R.id.button_add);
//        BlockButton = (Button) findViewById(R.id.button_block);

        RemainingMoney = findViewById(R.id.remaining_money);
        LastUsed = findViewById(R.id.last_used);
        ValidUntil = findViewById(R.id.valid_until);
    }
}
