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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import com.sousheelvunnam.scrimmage.adapters.GameAdapter;
import com.sousheelvunnam.scrimmage.ui.fragments.NavDrawerFragmentGames;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.Date;
import java.util.List;

public class GameActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener , NavDrawerFragmentGames.NavigationDrawerCallbacks {

    LatLng mLocation;
    GoogleApiClient mGoogleApiClient;
    Spinner mLocationSpinner;
    RecyclerView mRecyclerView;
    List<ParseObject> mScrimmages;
    private NavDrawerFragmentGames mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mNavigationDrawerFragment = (NavDrawerFragmentGames)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocation = new LatLng(0,0);
        mGoogleApiClient.connect();

        mLocationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mile_selections, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(spinnerAdapter);
        mLocationSpinner.setOnItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    private void retrieveScrimmages(double distance, final boolean changed) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        ParseGeoPoint geoPoint = new ParseGeoPoint(mLocation.latitude, mLocation.longitude);
        query.whereWithinMiles(ParseConstants.KEY_SCRIMMAGE_LATLNG, geoPoint, distance);
        Date now = new Date(System.currentTimeMillis());
        query.whereGreaterThan(ParseConstants.KEY_SCRIMMAGE_DATE, now);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scrimmages, ParseException e) {
                if (e == null) {
                    //success
                    mScrimmages = scrimmages;

                    GameAdapter adapter = new GameAdapter(mScrimmages, GameActivity.this);
                    if (changed) {
                        adapter.notifyDataSetChanged();
                    }
                    mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
    /**
     * Start of stuff from Nav Drawer
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 0) {
            Intent intent = new Intent(this, MyActivity.class);
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
        actionBar.setTitle(R.string.title_activity_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Inflate the menu; this adds items to the action bar if it is present.if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_game, menu);
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

        switch (id) {
            case R.id.action_create_scrimmage:
                Intent intent = new Intent(this, CreateGameActivity.class);
                intent.putExtra("from", "GameActivity");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                retrieveScrimmages(10, true);
                break;
            case 2:
                retrieveScrimmages(25, true);
                break;
            case 3:
                retrieveScrimmages(50, true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLocation = new LatLng(location.getLatitude(), location.getLongitude());

        retrieveScrimmages(10, false);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
