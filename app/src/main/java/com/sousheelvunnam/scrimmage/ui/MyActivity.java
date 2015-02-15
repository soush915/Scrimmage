package com.sousheelvunnam.scrimmage.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.adapters.HomeAdapter;
import com.sousheelvunnam.scrimmage.ui.fragments.NavDrawerFragment;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavDrawerFragment.NavigationDrawerCallbacks{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private List<ParseObject> mYourGames;
    private List<ParseObject> mNearbyGames;
    private RecyclerView mYourRecycler;
    private RecyclerView mNearbyRecycler;
    private Button mCreateScrimmageButton;
    private NavDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        buildGoogleApiClient();

        /*NavDrawer navDrawer = new NavDrawer();
        navDrawer.navDrawerOnCreate(this, getWindow().getDecorView().findViewById(android.R.id.content));*/
        mNavigationDrawerFragment = (NavDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        }
        mGoogleApiClient.connect();

        mCreateScrimmageButton = (Button) findViewById(R.id.homeCreateScrimmageButton);
        mCreateScrimmageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MyActivity.this, CreateGameActivity.class);
                Intent intent = new Intent(MyActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        mYourRecycler = (RecyclerView) findViewById(R.id.yourGameList);
        mYourRecycler.setHasFixedSize(false);
        RecyclerView.LayoutManager yourLayoutManager = new LinearLayoutManager(this);
        mYourRecycler.setLayoutManager(yourLayoutManager);
        retrieveScrimmages("yourGames");

        mNearbyRecycler = (RecyclerView) findViewById(R.id.nearbyGameList);
        mNearbyRecycler.setHasFixedSize(false);
        RecyclerView.LayoutManager nearbyLayoutManager = new LinearLayoutManager(this);
        mNearbyRecycler.setLayoutManager(nearbyLayoutManager);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void retrieveScrimmages(final String reason) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        Date now = new Date(System.currentTimeMillis());
        query.whereGreaterThan(ParseConstants.KEY_SCRIMMAGE_DATE, now);

        //TODO change results based on your games or nearby games
        if (reason.equals("yourGames")) {
            query.whereEqualTo(ParseConstants.KEY_CREATOR_ID, ParseUser.getCurrentUser().getObjectId());
            query.whereEqualTo(ParseConstants.KEY_RECIPIENTS_ID, ParseUser.getCurrentUser().getObjectId());
        }
        else if (reason.equals("nearbyGames")) {
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            ParseGeoPoint geoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);
            query.whereWithinMiles(ParseConstants.KEY_SCRIMMAGE_LATLNG, geoPoint, 10);
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(now);
            dateCalendar.add(Calendar.DATE, 7);
            query.whereLessThan(ParseConstants.KEY_SCRIMMAGE_DATE, now);
            query.setLimit(5);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scrimmages, ParseException e) {
                if (e == null) {
                    //success
                    if (reason.equals("yourGames")) {
                        mYourGames = scrimmages;
                        HomeAdapter mYourAdapter = new HomeAdapter(mYourGames);
                        mYourRecycler.setAdapter(mYourAdapter);
                    }
                    else if (reason.equals("nearbyGames")) {
                        mNearbyGames = scrimmages;
                        HomeAdapter mNearbyAdapter = new HomeAdapter(mNearbyGames);
                        mNearbyRecycler.setAdapter(mNearbyAdapter);
                    }
                }
            }
        });
    }

    /**
     * Start of stuff from Nav Drawer
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 1) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        else if (position == 2) {
            Intent intent = new Intent(this, MyAccountActivity.class);
            intent.putExtra(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
            startActivity(intent);
        }
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Inflate the menu; this adds items to the action bar if it is present.if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.my, menu);
        restoreActionBar();
        return true;
    }
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * End of stuff from Nav Drawer
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigateToLogin();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        retrieveScrimmages("nearbyGames");
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
