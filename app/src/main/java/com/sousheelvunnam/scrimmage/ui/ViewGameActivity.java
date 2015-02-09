package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.Date;
import java.util.List;

public class ViewGameActivity extends ActionBarActivity {

    private ImageView locationImage;
    private TextView titleTextView;
    private TextView sportTextView;
    private TextView descriptionTextView;
    private TextView dateTimeTextView;
    private TextView usernameTextView;
    private TextView addreassTextView;
    private Button directionsButton;
    private Button imGoingButton;
    private String address;

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
        addreassTextView = (TextView) findViewById(R.id.viewGameAddressText);
        directionsButton = (Button) findViewById(R.id.directionsButton);

        retrieveData();

        String objectId = getIntent().getStringExtra("OBJECT_ID");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        ParseObject game = null;
        try {
            game = query.get(objectId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ParseObject finalGame = game;

        //if (finalGame.getString(ParseConstants))
        imGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalGame != null) {
                    finalGame.addUnique(ParseConstants.KEY_RECIPIENTS_ID, ParseUser.getCurrentUser().getObjectId());
                    finalGame.saveInBackground();
                    Toast.makeText(ViewGameActivity.this, R.string.im_going_success, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewGameActivity.this, GameActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ViewGameActivity.this, R.string.generic_error_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0").buildUpon().appendQueryParameter("q", address).build();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
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
        address = intent.getStringExtra(ParseConstants.KEY_SCRIMMAGE_ADDRESS);


        Date date = new Date();
        date.setTime(intent.getLongExtra(ParseConstants.KEY_SCRIMMAGE_DATE, -1));

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        sportTextView.setText(sport);
        dateTimeTextView.setText(date.toString());
        usernameTextView.setText(username);
        addreassTextView.setText(address);
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
