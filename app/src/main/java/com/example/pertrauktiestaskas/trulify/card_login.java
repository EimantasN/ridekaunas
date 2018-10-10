package com.example.pertrauktiestaskas.trulify;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
import com.example.pertrauktiestaskas.methods.ByteUtils;
import com.example.pertrauktiestaskas.methods.ClassicCard;
import com.example.pertrauktiestaskas.methods.ClassicCardKeys;
import com.example.pertrauktiestaskas.methods.ClassicTagReader;
import com.example.pertrauktiestaskas.methods.RawClassicCard;
import com.example.pertrauktiestaskas.methods.TalinCard;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.pertrauktiestaskas.methods.NfcClass.bytesToHex;

public class card_login extends AppCompatActivity {

    TextView Status;
    TextView InfoText;
    TextView AccountInfo;
    ImageView Image;
    ProgressBar Loading;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private String serialId = "";
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    NfcAdapter adapter;

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

        adapter = NfcAdapter.getDefaultAdapter(this);
        onNewIntent(this.getIntent());

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

    @SuppressLint("NewApi")
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equalsIgnoreCase(action)) {
            try {
                byte[] tagId = tag.getId();
                ClassicCardKeys c = ClassicCardKeys.fromUserInput(
                        ByteUtils.hexStringToByteArray("D3F7D3F7D3F7"),
                        ByteUtils.hexStringToByteArray("FFFFFFFFFFFF"));
                ClassicTagReader tr = new ClassicTagReader(tagId, tag, c);
                RawClassicCard card = tr.readTag(tagId, tag, tr.getTech(tag), c);
                ClassicCard cc = card.parse();
                TalinCard tc = new TalinCard(cc);
                tc.getId();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Toast.makeText(this, "This tag is not supported. Action: " + action, Toast.LENGTH_LONG).show();
        }
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