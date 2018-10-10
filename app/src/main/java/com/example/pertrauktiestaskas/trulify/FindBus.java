package com.example.pertrauktiestaskas.trulify;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pertrauktiestaskas.methods.BusApiHandler;
import com.example.pertrauktiestaskas.methods.BusThread;
import com.example.pertrauktiestaskas.models.RootObject;
import com.example.pertrauktiestaskas.models.Route;
import com.example.pertrauktiestaskas.models.TrafiListModel;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.io.EOFException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FindBus extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //First To city recycleView
    private static ArrayList<TrafiListModel> ToCityList;

    private RecyclerView.LayoutManager mLayoutManagerToCity;
    private static RecyclerView.Adapter mAdapterToCity;
    private static RecyclerView mRecyclerViewToCity;
    LocationManager locationManager;
    BusApiHandler handler;

    public String Longitude;
    public String Latitude;

    public boolean Active =false;

    Button button;

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

        button = findViewById(R.id.button);


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

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        Button Search = findViewById(R.id.button);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchRoute("54.8922995", "23.9246517");
            }
        });

        EditText DateText = findViewById(R.id.editText);
        DateFormat df = new SimpleDateFormat("HH:mm");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        DateText.setText(reportDate);

        CardView FavoriteRoute = findViewById(R.id.favorite1);
        FavoriteRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFavorite();
            }
        });

        CardView FavoriteRoute2 = findViewById(R.id.favorite2);
        FavoriteRoute2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFavorite2();
            }
        });

        handler = new BusApiHandler();
    }

    public void LoadFavorite()
    {
        TextInputEditText text = findViewById(R.id.ToTextInput2);
        text.setText("Studentų g. 67");

        //Studentų 67
        SearchRoute("54.90300", "23.960314");
    }
    public void LoadFavorite2()
    {
        TextInputEditText text = findViewById(R.id.ToTextInput2);
        text.setText("Vytauto pr. 8");

        //Vytauto pr
        SearchRoute("54.8922995", "23.9246517");
    }


    public void SearchRoute(String EndLatidute, String EndLongitude) {

        button.setVisibility(button.GONE);
        Longitude = EndLatidute;
        Latitude = EndLongitude;

        for(int i =0; i < ToCityList.size(); i++)
        {
            ToCityList.remove(i);
            mAdapterToCity.notifyItemRemoved(i);
        }
        if(ToCityList.size() != 0) {
            ToCityList.clear();
            mAdapterToCity.notifyDataSetChanged();
        }

        ToCityList.add(null);
        mAdapterToCity.notifyItemInserted(ToCityList.size() - 1);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
//loc            editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            //Toast.makeText(
                    //getBaseContext(),
                    //"Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            //+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = ""+loc.getLongitude();
            //Log.v(TAG, longitude);
            String latitude = ""+loc.getLatitude();
            //Log.v(TAG, latitude);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    //cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude.replace("Longitude: ", "") + "\n" + latitude.replace("Latitude: ", "") + "\n\nMy Current City is: ";
            //editLocation.setText(s);

            Toast.makeText(
                    getBaseContext(), s, Toast.LENGTH_SHORT).show();

            if(!Active) {
                Active=true;
                new LongOperation().execute(latitude, longitude);
            }

        }

        private class LongOperation extends AsyncTask<String, Void, String> {

            List<TrafiListModel> data;
            @Override
            protected String doInBackground(String... params) {
                try {
                    RootObject datax = BusApiHandler.GetRouteData(params[0], params[1], Longitude, Latitude);
                    data = handler.FormatRoutesToListModel(datax);
                }
                catch (Exception e)
                {
                    String vc = e.getMessage();
                }

                return "Executed";
            }

            @Override
            protected void onPostExecute(String result) {

                for(int i =0; i < ToCityList.size(); i++)
                {
                    ToCityList.remove(i);
                    mAdapterToCity.notifyItemRemoved(i);
                }
                if(ToCityList.size() != 0) {
                    ToCityList.clear();
                    mAdapterToCity.notifyDataSetChanged();
                }

                if(data != null) {
                    for (TrafiListModel a : data) {
                        ToCityList.add(a);
                        mAdapterToCity.notifyItemInserted(ToCityList.size() - 1);
                    }
                }
                else
                {
                    Toast.makeText(
                    getBaseContext(), "Nepavyko gauti informacijos", Toast.LENGTH_SHORT).show();
                }

                button.setVisibility(button.VISIBLE);

                Active = false;
            }

            @Override
            protected void onPreExecute() {}

            @Override
            protected void onProgressUpdate(Void... values) {}
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
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

        if (id == R.id.nav_dashboard) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("studentId", "1");
            startActivity(i);
        } else if (id == R.id.nav_tickets) {

        } else if (id == R.id.nav_search) {


        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_cards) {

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
                if(TrafiList.get(position).EndStreet.isEmpty())
                {
                    myHolder.mEndStreet.setText("Atvykote");
                }
                else {
                    myHolder.mEndStreet.setText(TrafiList.get(position).EndStreet);
                }
                myHolder.mEndTime.setText(TrafiList.get(position).EndTime);
                myHolder.mStartTime.setText(TrafiList.get(position).StartTime);
                myHolder.mNextStopTime.setText(TrafiList.get(position).NextStopTime);

                    myHolder.mNextStopDistance.setText(TrafiList.get(position).NextStopDistance);
                if(!TrafiList.get(position).NextStopDistance.trim().equals("0")) {
                    myHolder.mImageBottomDistance.setText(TrafiList.get(position).ImageBottomDistance);
                }
                else
                    myHolder.mImageBottomDistance.setText("");

                if (!TrafiList.get(position).Image.contains("walksegment"))
                {
                    Picasso.get().load(TrafiList.get(position).Image).resize(50, 50).centerCrop().into(myHolder.mImage);
                }

            }
        }
    }
}
