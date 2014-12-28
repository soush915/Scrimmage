package com.sousheelvunnam.scrimmage.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject scrimmage = mScrimmages.get(position);

        Intent intent = new Intent(this, ViewGameActivity.class);
        //intent.putExtra("OBJECT_ID", scrimmage.getObjectId());
        intent.putExtra(ParseConstants.KEY_CREATOR_USERNAME, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_TITLE, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_TITLE));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DESCRIPTION));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_DAY, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_DAY));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_MONTH, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_MONTH));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_YEAR, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_YEAR));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_MINUTE, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_MINUTE));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_HOUR, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_HOUR));
        intent.putExtra(ParseConstants.KEY_SCRIMMAGE_SPORT, scrimmage.getString(ParseConstants.KEY_SCRIMMAGE_SPORT));
        startActivity(intent);
    }*/

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
