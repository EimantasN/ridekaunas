package com.example.pertrauktiestaskas.trulify;

import com.example.pertrauktiestaskas.methods.NfcClass;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pertrauktiestaskas.methods.BusThread;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String serialId = "";
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    NfcAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.toobalFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                YoYo.with(Techniques.RotateInDownLeft)
                        .duration(700)
                        .repeat(5)
                        .playOn(findViewById(R.id.toobalFab));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapter = NfcAdapter.getDefaultAdapter(this);
        onNewIntent(this.getIntent());

//        RootObject data = BusApiHandler.GetRouteData("54.901694", "23.961288", "54.893767", "23.925123");
//        Log.d("TG", data.toString());
        //Intent launchNewIntent = new Intent(MainActivity.this, NfcClass.class);
        //startActivityForResult(launchNewIntent, 0);
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
        Toast.makeText(this,""+intent.getAction(), Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equalsIgnoreCase(action)) {
            try {
                byte[] tagId = tag.getId();
                serialId = bytesToHex(tagId);

            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        } else {
            Toast.makeText(this, "This tag is not supported. Action: " + action, Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(getApplicationContext(), FindBus.class);
            i.putExtra("studentId", "1");
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(getApplicationContext(), FindBus.class);
            i.putExtra("studentId", "1");
            startActivity(i);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
