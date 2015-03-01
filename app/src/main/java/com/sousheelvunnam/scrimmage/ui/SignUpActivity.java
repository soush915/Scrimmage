package com.sousheelvunnam.scrimmage.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

public class SignUpActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;
    protected EditText mName;
    protected EditText mFavoriteSport;
    protected EditText mPhone;
    private GoogleApiClient mGoogleApiClient;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);
        buildGoogleApiClient();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mName = (EditText) findViewById(R.id.nameEditText);
        mFavoriteSport = (EditText) findViewById(R.id.favoriteSportEditText);
        mPhone = (EditText) findViewById(R.id.phoneEditText);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mCancelButton =(Button)findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String name = mName.getText().toString();
                String favoriteSport = mFavoriteSport.getText().toString();
                String phone = mPhone.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();
                name = name.trim();
                favoriteSport = favoriteSport.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()  || name.isEmpty() || favoriteSport.isEmpty() || phone.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(getString(R.string.sign_up_error_message))
                            .setTitle(getString(R.string.sign_up_error_title))
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    // create new user
                    setSupportProgressBarIndeterminateVisibility(true);

                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.put(ParseConstants.KEY_NAME, name);
                    newUser.put(ParseConstants.KEY_FAVORITE_SPORT, favoriteSport);
                    newUser.put(ParseConstants.KEY_PHONE, phone);

                    //Gets last location of user - is usually the current
        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLastLocationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastLocationLatLng, 15));
        }*/

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);

                            if (e==null) {
                                //Successful Sign Up
                                Intent intent = new Intent(SignUpActivity.this, MyActivity.class);
                                intent.putExtra(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
                                startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(getString(R.string.sign_up_error_title))
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Needed for location API
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
