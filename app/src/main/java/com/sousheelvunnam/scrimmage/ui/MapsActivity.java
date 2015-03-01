package com.sousheelvunnam.scrimmage.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng mLastLocationLatLng;
    private Marker mScrimmageLocationMarker;
    private LatLng mFinalLocation;
    private Bitmap mLocationBitmap;
    private String mAddress = "No address found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reverse geocodes address from location of marker
                mMap.clear();
                if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.GINGERBREAD
                        &&
                        Geocoder.isPresent()) {
                    (new GetAddressTask(MapsActivity.this)).execute(latLng);
                }
                mScrimmageLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(mAddress));
                mScrimmageLocationMarker.hideInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 500, null);
                mFinalLocation = latLng;
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        mLocationBitmap = bitmap;
                    }
                });
            }
        });
    }


    /**
     * Methods for implementing GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
     *      needed for Location API
     */
    @Override
    public void onConnected(Bundle bundle) {
        //Gets last location of user - is usually the current
        Toast.makeText(this, "SWIGGITY CONNECTED", Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLastLocationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastLocationLatLng, 17));
            mMap.addMarker(new MarkerOptions().position(mLastLocationLatLng).title("WEEWEWEWEWEWEWEWEWEWEWEW"));
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Connection failed. Please try again.", Toast.LENGTH_LONG).show();
    }










    /**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
    public class GetAddressTask extends
            AsyncTask<LatLng, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            LatLng latlng = params[0];
            // Create a list to contain the result address
            List<Address> address = null;
            try {
                /*
                 * Return 1 address.
                 */
                address = geocoder.getFromLocation(latlng.latitude,
                        latlng.longitude, 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                /*String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;*/
            }
            // If the reverse geocode returned an address
            if (address != null) {
                // Get the first address
                Address mainAddress = address.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s",
                        // If there's a street address, add it
                        mainAddress.getMaxAddressLineIndex() > 0 ?
                                mainAddress.getAddressLine(0) : ""
                        /*, // Locality is usually a city
                        mainAddress.getLocality(),
                        // The country of the address
                        mainAddress.getCountryName()*/);
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }
        /**
         * A method that's called once doInBackground() completes. Turn
         * off the indeterminate activity indicator and set
         * the text of the UI element that shows the address. If the
         * lookup failed, display the error message.
         */
        @Override
        protected void onPostExecute(String address) {
           mAddress = address;
        }
    }













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle(R.string.search_title)
                    .setSingleChoiceItems(R.array.search_items, null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.action_search, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create();
            AlertDialog dialog = builder.create();
            dialog.show();*/

            Uri gmmURi = Uri.parse("geo:" + mLastLocationLatLng.latitude + "," + mLastLocationLatLng.longitude + "?z=10&q=parks");;
            Intent parkIntent = new Intent(Intent.ACTION_VIEW, gmmURi);
            parkIntent.setPackage("com.google.android.apps.maps");
            startActivity(parkIntent);
        }
        else if (id == R.id.action_save_location) {
            Intent saveIntent = new Intent(MapsActivity.this, CreateGameActivity.class);
            /*if (mFinalLocation != null /*&& mLocationBitmap != null) {
                saveIntent.putExtra("locationLatitude", mFinalLocation.latitude);
                saveIntent.putExtra("locationLongitude", mFinalLocation.longitude);
            }
            else {
                saveIntent.putExtra("locationLatitude", "null");
                saveIntent.putExtra("locationLongitude", "null");
            }*/
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics());
            float roundedpx = Math.round(px);
            int height = (int)roundedpx;
            int bitmapStartHeight = ((mLocationBitmap.getHeight() / 2) - (height/2));
            Bitmap finalBitmap = Bitmap.createBitmap(mLocationBitmap, 0, bitmapStartHeight, mLocationBitmap.getWidth(), height);
            final ParseObject scrimmage = new ParseObject(ParseConstants.CLASS_SCRIMMAGES);
            byte[] bytearray;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bytearray = stream.toByteArray();

            saveIntent.putExtra("locationLatitude", mFinalLocation.latitude);
            saveIntent.putExtra("locationLongitude", mFinalLocation.longitude);
            saveIntent.putExtra("address", mAddress);
            saveIntent.putExtra("pictureByteArray", bytearray);
            saveIntent.putExtra("from", "MapsActivity");
            startActivity(saveIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
