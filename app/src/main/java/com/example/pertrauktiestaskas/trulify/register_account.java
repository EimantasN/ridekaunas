package com.example.pertrauktiestaskas.trulify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class register_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Button AddCard = (Button) findViewById(R.id.button5);

        AddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("studentId", "1");
        startActivity(i);
    }
}
