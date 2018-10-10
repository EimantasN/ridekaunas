package com.example.pertrauktiestaskas.trulify;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class cardlist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton addCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addCards = findViewById(R.id.fab);

        addCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCardActivity();
            }
        });
    }

    private void AddCardActivity() {
        Intent i = new Intent(getApplicationContext(), card_login.class);
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("studentId", "1");
            startActivity(i);
        } else if (id == R.id.nav_tickets) {

        } else if (id == R.id.nav_search) {
            Intent i = new Intent(getApplicationContext(), FindBus.class);
            i.putExtra("studentId", "1");
            startActivity(i);


        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_cards) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
