package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.Parse;
import com.parse.ParseUser;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

public class EditProfileActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    protected EditText bioEdit;
    protected EditText locationEdit;
    protected Button currentLocation;
    protected EditText favoriteSportEdit;
    protected EditText phoneEdit;
    protected Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        buildGoogleApiClient();

        bioEdit = (EditText) findViewById(R.id.bioEditText);
        locationEdit = (EditText) findViewById(R.id.locationEditText);
        currentLocation = (Button) findViewById(R.id.currentLocationButton);
        favoriteSportEdit = (EditText) findViewById(R.id.favoriteSportEditProfileText);
        phoneEdit = (EditText) findViewById(R.id.phoneEditProfileText);
        saveButton = (Button) findViewById(R.id.saveProfileButton);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bio = bioEdit.getText().toString();
                String location = locationEdit.getText().toString();
                String favoriteSport = favoriteSportEdit.getText().toString();
                String phone = phoneEdit.getText().toString();

                ParseUser user = ParseUser.getCurrentUser();
                user.put(ParseConstants.KEY_BIO, bio);
                user.put(ParseConstants.KEY_LOCATION, location);
                user.put(ParseConstants.KEY_FAVORITE_SPORT, favoriteSport);
                user.put(ParseConstants.KEY_PHONE, phone);

                user.saveInBackground();

                Toast.makeText(EditProfileActivity.this, R.string.account_success, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditProfileActivity.this, MyAccountActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
