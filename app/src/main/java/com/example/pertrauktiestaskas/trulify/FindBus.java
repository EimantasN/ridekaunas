package com.example.pertrauktiestaskas.trulify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pertrauktiestaskas.models.Route;
import com.example.pertrauktiestaskas.models.TrafiListModel;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FindBus extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //First To city recycleView
    private static ArrayList<TrafiListModel> ToCityList;

    private RecyclerView.LayoutManager mLayoutManagerToCity;
    private static RecyclerView.Adapter mAdapterToCity;
    private static RecyclerView mRecyclerViewToCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bus);
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


        ToCityList = new ArrayList<TrafiListModel>();
        mRecyclerViewToCity = findViewById(R.id.FastRoute);
        mLayoutManagerToCity = new LinearLayoutManager(this);
        mRecyclerViewToCity.setLayoutManager(mLayoutManagerToCity);
        mAdapterToCity = new RecyclerAdapterTrafi(ToCityList, mRecyclerViewToCity, this);
        mRecyclerViewToCity.setAdapter(mAdapterToCity);

        ToCityList.add(new TrafiListModel());
        mAdapterToCity.notifyItemInserted(ToCityList.size() - 1);


        ToCityList.add(null);
        mAdapterToCity.notifyItemInserted(ToCityList.size() - 1);
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

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class RecyclerAdapterTrafi extends RecyclerView.Adapter
    {
        private ArrayList<TrafiListModel> TrafiList;
        private RecyclerView mRecyclerView;
        private Context RVContext;

            public RecyclerAdapterTrafi(ArrayList<TrafiListModel> mList, RecyclerView recyclerView, Context context)
        {
            TrafiList = mList;
            mRecyclerView = recyclerView;
            RVContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == R.layout.loading)
            {
                View Loading = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false);

                Loading view = new Loading(Loading) { };

                return view;
            }
            else
            {
                View TrafiViewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.directions, parent, false);

                TextView EndStreet = (TextView)TrafiViewList.findViewById(R.id.EndStreet);
                TextView EndTime = TrafiViewList.findViewById(R.id.EndTime);
                TextView StartTime = TrafiViewList.findViewById(R.id.StartTime);
                TextView NextStopTime = TrafiViewList.findViewById(R.id.NextStopTime);
                TextView NextStopDistance = TrafiViewList.findViewById(R.id.NextStopDistance);
                TextView ImageBottomDistance = TrafiViewList.findViewById(R.id.ImageBottomDistance);
                ImageView Image = TrafiViewList.findViewById(R.id.Image);

//                TrafiListview view = new TrafiListview(TrafiViewList)
//                {
//                    mEndStreet = EndStreet,
//                    mEndTime = EndTime,
//                    mStartTime = StartTime,
//                    mNextStopTime = NextStopTime,
//                    mNextStopDistance = NextStopDistance,
//                    mImageBottomDistance = ImageBottomDistance,
//                    mImage = Image
//                };

                return new TrafiListview(TrafiViewList);
            }
        }

        @Override
        public int getItemCount() {
            return TrafiList.size();
        }

        public class Loading extends RecyclerView.ViewHolder
        {
            public View LoadingView;

            public Loading(View view)
            {
                super(view);
                LoadingView = view;
            }
        }

        public class TrafiListview extends RecyclerView.ViewHolder
        {
            public View ConversationListHolder;
            public TextView mEndStreet;
            public TextView mEndTime;
            public TextView mStartTime;
            public TextView mNextStopTime ;
            public TextView mNextStopDistance ;
            public TextView mImageBottomDistance ;
            public ImageView mImage ;

                public TrafiListview(View view)
            {
                super(view);
                ConversationListHolder = view;
                 mEndStreet = view.findViewById(R.id.EndStreet);
                 mEndTime = view.findViewById(R.id.EndTime);
                 mStartTime = view.findViewById(R.id.StartTime);
                 mNextStopTime = view.findViewById(R.id.NextStopTime);
                 mNextStopDistance = view.findViewById(R.id.NextStopDistance);
                 mImageBottomDistance = view.findViewById(R.id.ImageBottomDistance);
                mImage = view.findViewById(R.id.Image);
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            if (TrafiList.get(position) == null)
            {
                return R.layout.loading;
            }
            else
            {
                return R.layout.directions;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (holder.getItemViewType() == R.layout.loading)
            {
                ;
            }
            else
            {
                TrafiListview myHolder = (TrafiListview)holder;
                myHolder.mEndStreet.setText(TrafiList.get(position).StartTime);;
                myHolder.mEndTime.setText(TrafiList.get(position).EndTime);
                myHolder.mStartTime.setText(TrafiList.get(position).StartTime);
                myHolder.mNextStopTime.setText(TrafiList.get(position).NextStopTime);
                myHolder.mNextStopDistance.setText(TrafiList.get(position).NextStopDistance);
                myHolder.mImageBottomDistance.setText(TrafiList.get(position).ImageBottomDistance);

                if (!TrafiList.get(position).Image.contains("walksegment"))
                {
                    Picasso.get().load(TrafiList.get(position).Image).resize(50, 50).centerCrop().into(myHolder.mImage);
                }

            }
        }
    }
}
