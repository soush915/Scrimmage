package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.Date;
import java.util.List;

public class ViewGameActivity extends Activity {

    private ImageView locationImage;
    private TextView titleTextView;
    private TextView sportTextView;
    private TextView descriptionTextView;
    private TextView dateTimeTextView;
    private TextView usernameTextView;
    private Button imGoingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        titleTextView = (TextView) findViewById(R.id.viewGameTitleTextView);
        sportTextView = (TextView) findViewById(R.id.viewGameSportTextView);
        descriptionTextView = (TextView) findViewById(R.id.viewGameDescriptionTextView);
        dateTimeTextView = (TextView) findViewById(R.id.viewGameDateTimeTextView);
        usernameTextView = (TextView) findViewById(R.id.viewGameCreatorTextView);
        imGoingButton = (Button) findViewById(R.id.imGoingButton);

        retrieveData();

        imGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_game, menu);
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

    private void retrieveData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra(ParseConstants.KEY_CREATOR_USERNAME);
        String title = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_TITLE);
        String description = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION);
        String sport = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_SPORT);
        String day = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_DAY);
        String month = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_MONTH);
        String year = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_YEAR);
        String minute = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_MINUTE);
        String hour = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_HOUR);

        Date date = new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),  Integer.parseInt(minute), Integer.parseInt(hour));

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        sportTextView.setText(sport);
        dateTimeTextView.setText(date.toString());
        usernameTextView.setText(username);

    }

    /*private String[] retrieveData() {
        String[] data = new String[4];   //Change later for more data like location image and people images

        Intent intent = getIntent();
        String objectId = intent.getStringExtra("OBJECT_ID");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        try {
            query.get(objectId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }



        data[0] = query.;  //Title
        data[1] = "Sport";  //Sport
        data[2] = "";  //Description
        data[3] = "";  //Date+Time

        return data;
    }*/
}
