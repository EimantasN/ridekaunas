package com.example.pertrauktiestaskas.trulify;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.pertrauktiestaskas.API.api_requests;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class splash_screen extends AppCompatActivity {

    api_requests Calls;
    TextView Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Email = findViewById(R.id.email);

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");

        if(list.length > 0) {
            Email.setText(list[0].name);
            api_requests.currentUserGmail = list[0].name;
        }
        else
        {
            GotoCardAddActivity();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //LoginIfAcountFound();
    }

    public void LoginIfAcountFound()
    {
        if (TextUtils.isEmpty(api_requests.currentUserGmail)) {
            //Nera prijungtu vartotoju
            GotoCardAddActivity();
        } else {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(api_requests.currentUserGmail).matches())
            {
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        final int ID = Calls.Login(api_requests.currentUserGmail);

                        if(ID > 0)  //Jeigu jau registruotas
                        {
                            //Perkeliu i main activity
                            GoToMainActivity();
                        }
                        else if(ID == -2)//Jeigu naujas vartotojas
                        {
                            GotoCardAddActivity();
                        }
                        else
                        {
                            GotoAccountChooseActivity();
                        }


                    }
                }).start();
            }
            else
            {
                GotoCardAddActivity();
            }
        }
    }

    public void GoToMainActivity()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void GotoAccountChooseActivity()
    {
        Intent i = new Intent(getApplicationContext(), card_login.class);
        startActivity(i);
    }

    public void GotoCardAddActivity()
    {
        Intent i = new Intent(getApplicationContext(), Register.class);
        startActivity(i);
    }
}




