package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.fragments.DatePickerFragment;
import com.sousheelvunnam.scrimmage.fragments.TimePickerFragment;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateGameActivity extends Activity {

    private ImageButton mLocationImageButton;
    private Button mSubmitButton;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mSportEditText;
    private TextView mAddressTextView;

    private ParseGeoPoint mLatLng;
    private String mAddress;
    private String mDay;
    private String mMonth ;
    private  String mYear;
    private String mHour;
    private String mMinute;

    private Bitmap mBitmap;

    private ParseObject mScrimmage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);


        mTitleEditText = (EditText) findViewById(R.id.titleEditText);
        mDescriptionEditText =(EditText) findViewById(R.id.descriptionEditText);
        mSportEditText = (EditText) findViewById(R.id.sportEditText);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mDateButton = (Button) findViewById(R.id.dateButton);
        mTimeButton = (Button) findViewById(R.id.timeButton);
        mLocationImageButton =(ImageButton) findViewById(R.id.locationImageButton);
        mAddressTextView = (TextView) findViewById(R.id.addressText);


        updateAddress();

        mLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGameActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject scrimmage = createScrimmage();
                if (scrimmage==null) {
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateGameActivity.this);
                    builder.setMessage(R.string.generic_error_message).setTitle(R.string.generic_error_title).setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    save(scrimmage);
                    Intent intent = new Intent(CreateGameActivity.this, GameActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog mDialog = new DatePickerDialog(CreateGameActivity.this, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog mDialog = new TimePickerDialog(CreateGameActivity.this, mTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                mDialog.show();
            }
        });
    }

    private void updateAddress() {
        Intent locationIntent = getIntent();
        /*String address = "";
        if (locationIntent != null) {
            //TODO get bitmap image of map and put here and reverse geocode address at point given
            Double latitude = locationIntent.getDoubleExtra("locationLatitude", 0);
            Double longitude = locationIntent.getDoubleExtra("locationLongitude", 0);

            if (latitude == 0 || longitude == 0) {
                address = "wew";
                mAddressTextView.setText(address);
            }
            else {
                LatLng latLng = new LatLng(latitude, longitude);
                if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.GINGERBREAD
                        &&
                        Geocoder.isPresent()) {
                    (new GetAddressTask(CreateGameActivity.this)).execute(latLng);
                }
                mAddressTextView.setText(address);
            }

        }*/
        Double latitude = locationIntent.getDoubleExtra("locationLatitude", 0);
        Double longitude = locationIntent.getDoubleExtra("locationLongitude", 0);
        String latitudeeS = latitude.toString();
        String longitudeS = longitude.toString();
        mLatLng = new ParseGeoPoint(latitude, longitude);
        String address = locationIntent.getStringExtra("address");
        mAddress = address;
        mAddressTextView.setText(address + ", " + latitudeeS + ", " +longitudeS);

        Bitmap bitmap = locationIntent.getParcelableExtra("bitmap");
        mLocationImageButton.setImageBitmap(bitmap);
        mBitmap = bitmap;
    }


    @Override
    protected void onResume() {
        super.onResume();

        updateAddress();
    }

    protected ParseObject createScrimmage() {
        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        String sport = mSportEditText.getText().toString();

        mScrimmage = new ParseObject(ParseConstants.CLASS_SCRIMMAGES);

        mScrimmage.put(ParseConstants.KEY_CREATOR_ID, ParseUser.getCurrentUser().getObjectId());
        mScrimmage.put(ParseConstants.KEY_CREATOR_USERNAME, ParseUser.getCurrentUser().getUsername());
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_TITLE, title);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION, description);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_DAY, mDay);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_MONTH, mMonth);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_YEAR, mYear);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_HOUR, mHour);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_MINUTE, mMinute);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_SPORT, sport);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_ADDRESS, mAddress);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_LATLNG, mLatLng);
        mScrimmage.put(ParseConstants.KEY_SCRIMMAGE_PICTURE, mBitmap);

        return mScrimmage;
    }

    protected void save(ParseObject scrimmage) {
        scrimmage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    //success
                    Toast.makeText(CreateGameActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateGameActivity.this);
                    builder.setMessage(R.string.create_game_error_message).setTitle(R.string.generic_error_title).setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateButton.setText(monthOfYear + " - " + dayOfMonth + " - " + year);

            mDay = dayOfMonth + "";
            mMonth = monthOfYear + "";
            mYear = year + "";
        }
    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTimeButton.setText(hourOfDay + ": " + minute);

            mHour = hourOfDay + "";
            mMinute = minute + "";
        }
    };
}
