package com.example.pertrauktiestaskas.trulify;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pertrauktiestaskas.API.api_requests;
import com.example.pertrauktiestaskas.firebastModels.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class card_login extends AppCompatActivity {

    api_requests Calls;

    private String currentUserGmail = "";

    TextView Status;
    TextView InfoText;
    TextView AccountInfo;
    TextView googleButton;
    ImageView Image;
    ProgressBar Loading;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    SignInButton signInButton;

    public String googleAccountText = "Keisti vartotoją";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_login);

        Status = findViewById(R.id.statustext);
        InfoText = findViewById(R.id.infoText);
        Image = findViewById(R.id.image);
        Loading = findViewById(R.id.loading);
        AccountInfo = findViewById(R.id.accountinfo);

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddingCardAnimation();
            }
        } );

        LaodingHide();

        //Google sign in button and actions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");

        if(list.length > 0){
            AccountInfo.setText("Kortelė bus susieta su\n" + list[0].name);
            currentUserGmail = list[0].name;
        }
        else {
            AccountInfo.setText("Prisijunkite prie google paskyros, norėdami tęsti kortelės pridėjimą");
            googleAccountText = "Pridėti google paskyrą";
        }

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        googleButton = (TextView) signInButton.getChildAt(0);
        googleButton.setText(googleAccountText);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        } );
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void LaodingHide() { Loading.setVisibility(Loading.GONE); }

    public void LaodingShow() {Loading.setVisibility(Loading.VISIBLE);}

    public void ChangeStatusText(String text) {Status.setText(text);}

    public void AddingCardAnimation()
    {
        LaodingShow();
        ChangeStatusText("Nuskaitomi duomenys");
        YoYo.with(Techniques.Flash)
                .duration(5000)
                .repeat(Animation.INFINITE)
                .playOn(findViewById(R.id.image));

        Register();
        //GoToActivity();
    }

    public void Register()
    {
        if(!api_requests.userActive)
        {
            new Thread(new Runnable() {
                public void run() {
                    // a potentially time consuming task
                    final boolean status = api_requests.Register(api_requests.currentUserGmail);

                    if(status)
                    {
                        AccountInfo.post(new Runnable() {
                            public void run() {
                                AccountInfo.setText(api_requests.currentUserGmail + " susiejimas sėkmingas");
                            }
                        });
                    }
                    else
                    {
                        //TODO reiktu žinutės kas nutiko jei nepavyko vartotojo užregistruoti, bet beveik neimanoma kad taip nutiktu nebet (Concurrent)
                        AccountInfo.post(new Runnable() {
                            public void run() {
                                AccountInfo.setText("Nepavyko aktyvuoti "+ api_requests.currentUserGmail + " paskyros");
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public void GoToActivity()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {

            AccountInfo.setText("Klaida. Mėginkite dar kartą");
            googleButton.setText("Pridėti paskyrą");
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        InfoText.setText("Kortelė bus susieta su " + account.getEmail());
    }
}