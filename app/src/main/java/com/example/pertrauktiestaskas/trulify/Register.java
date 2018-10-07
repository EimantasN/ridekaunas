package com.example.pertrauktiestaskas.trulify;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    TextView CardName;
    TextView CartLastName;

    TextView IDText;
    TextView ValidTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Register");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_layout);

        Button AddCard = (Button) findViewById(R.id.button4);

        AddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCard();
            }
        });

        CardName = findViewById(R.id.textView11);
        CartLastName = findViewById(R.id.textView10);

        IDText = findViewById(R.id.textView13);
        ValidTime = findViewById(R.id.textView9);


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
                    CardName.setText(s.toString());
                else
                    CardName.setText("pavardenis");
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