package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.fragments.DatePickerFragment;
import com.sousheelvunnam.scrimmage.fragments.TimePickerFragment;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.awt.font.TextAttribute;

public class CreateGameActivity extends Activity {

    private ImageButton mLocationImageButton;
    private Button mSubmitButton;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mSportEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

       /* final DatePickerFragment datePickerFragment = new DatePickerFragment();
        mDate = datePickerFragment.mDay;

        mDateTextView = (TextView) findViewById(R.id.dateTextView);
        mDateTextView.setText("lol");

        mDateButton = (Button) findViewById(R.id.dateButton);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);

                mDate = datePickerFragment.mDay;
                mDateTextView.setText(mDate + "");

            }
        });*/

        mTitleEditText = (EditText) findViewById(R.id.titleEditText);
        mDescriptionEditText =(EditText) findViewById(R.id.descriptionEditText);
        mSportEditText = (EditText) findViewById(R.id.sportEditText);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mLocationImageButton =(ImageButton) findViewById(R.id.locationImageButton);

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
                    send(scrimmage);
                    finish();
                }
            }
        });
    }



    protected ParseObject createScrimmage() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        TimePickerFragment timePickerFragment = new TimePickerFragment();

        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        String day = datePickerFragment.mDay;
        String month = datePickerFragment.mMonth;
        String year = datePickerFragment.mYear;
        String hour = timePickerFragment.mHour;
        String minute = timePickerFragment.mMinute;
        String sport = mSportEditText.getText().toString();

        ParseObject scrimmage = new ParseObject(ParseConstants.CLASS_SCRIMMAGES);

        scrimmage.put(ParseConstants.KEY_CREATOR_ID, ParseUser.getCurrentUser().getObjectId());
        scrimmage.put(ParseConstants.KEY_CREATOR_USERNAME, ParseUser.getCurrentUser().getUsername());
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_TITLE, title);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION, description);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_DAY, day);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_MONTH, month);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_YEAR, year);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_HOUR, hour);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_MINUTE, minute);
        scrimmage.put(ParseConstants.KEY_SCRIMMAGE_SPORT, sport);

        return scrimmage;
    }

    protected void send(ParseObject scrimmage) {
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
