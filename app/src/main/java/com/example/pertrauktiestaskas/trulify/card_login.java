package com.example.pertrauktiestaskas.trulify;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

    TextView Status;
    TextView InfoText;
    TextView AccountInfo;
    ImageView Image;
    ProgressBar Loading;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");

        if(list.length > 0)
            AccountInfo.setText("Kortelė bus susieta su " + list[0].name);
        else
            AccountInfo.setText("Prisijugti prie google paskyros, norėdami tęsti kortelės pridėjimą");

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Kitas vartotojas");


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

        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void LaodingHide() {Loading.setVisibility(
            Loading.GONE);
    }

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

        GoToActivity();
    }

    public void GoToActivity()
    {
        Intent i = new Intent(getApplicationContext(), cardlist.class);
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

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        InfoText.setText("Kortelė bus susieta su " + account.getEmail());
        //writeNewUser(account.getId(), account.getDisplayName(), account.getFamilyName());
    }
}