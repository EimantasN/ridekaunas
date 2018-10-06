package com.example.pertrauktiestaskas.trulify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_layout);

        Button AddCard = (Button) findViewById(R.id.button4);

        AddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCard();
            }
        });
    }

    public void AddCard()
    {
        Intent i = new Intent(getApplicationContext(), register_account.class);
        i.putExtra("studentId", "1");
        startActivity(i);
    }
}