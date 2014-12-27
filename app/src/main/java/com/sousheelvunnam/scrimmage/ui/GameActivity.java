package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.adapters.GameAdapter;
import com.sousheelvunnam.scrimmage.util.NavDrawer;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

import java.util.List;
import java.util.ListResourceBundle;

public class GameActivity extends Activity {

    RecyclerView mRecyclerView;
    List<ParseObject> mScrimmages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        NavDrawer navDrawer = new NavDrawer();
        navDrawer.navDrawerOnCreate(this, getWindow().getDecorView().findViewById(android.R.id.content));

        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scrimmages, ParseException e) {
                if (e == null) {
                    //success
                    mScrimmages = scrimmages;

                    GameAdapter adapter = new GameAdapter(mScrimmages);
                    mRecyclerView.setAdapter(adapter);

                }
            }
        });


    }

    private void retrieveScrimmages () {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_SCRIMMAGES);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scrimmages, ParseException e) {
                if (e==null) {
                    //success
                    mScrimmages = scrimmages;

                    int size = mScrimmages.size();
                    String[] titles = new String[size];
                    String[] descriptions = new String[size];

                    int i = 0;
                    for (ParseObject scrimmage : mScrimmages) {
                        titles[i] = scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE);
                        descriptions[i] = scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION);
                        i++;
                    }
                    if (mRecyclerView.getAdapter()==null) {
                        GameAdapter adapter = new GameAdapter(mScrimmages);
                        mRecyclerView.setAdapter(adapter);
                    }
                    /*else {
                        //refill adapter
                        (mRecyclerView.getAdapter()).refill(messages);
                    }*/
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_scrimmage:
                Intent intent = new Intent(this, CreateGameActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
