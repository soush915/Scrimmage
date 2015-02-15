package com.sousheelvunnam.scrimmage.ui;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.ui.fragments.NavDrawerFragmentMyAccount;
import com.sousheelvunnam.scrimmage.util.ParseConstants;

public class MyAccountActivity extends ActionBarActivity implements
                NavDrawerFragmentMyAccount.NavigationDrawerCallbacks{

    private TextView mNameTextView;
    private TextView mUsernameTextView;
    private TextView mFavoriteSportTextView;
    private TextView mBioTextView;
    private TextView mLocationTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private NavDrawerFragmentMyAccount mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mNavigationDrawerFragment = (NavDrawerFragmentMyAccount)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mUsernameTextView = (TextView) findViewById(R.id.usernameTextView);
        mFavoriteSportTextView = (TextView) findViewById(R.id.favoriteSportTextView);
        mBioTextView = (TextView) findViewById(R.id.bioTextView);
        mLocationTextView = (TextView) findViewById(R.id.locationAccountTextView);
        mEmailTextView = (TextView) findViewById(R.id.emailTextView);
        mPhoneTextView = (TextView) findViewById(R.id.phoneTextView);

        Intent intent = getIntent();
        String objectId = intent.getStringExtra(ParseConstants.KEY_USER_ID);
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        ParseUser user =  null;
        try {
            user = userQuery.get(objectId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (user!=null) {
            mNameTextView.setText(user.getString(ParseConstants.KEY_NAME));
            mUsernameTextView.setText(user.getUsername());
            mFavoriteSportTextView.setText(user.getString(ParseConstants.KEY_FAVORITE_SPORT));
            if (user.getString(ParseConstants.KEY_BIO) != null) {
                mBioTextView.setText(user.getString(ParseConstants.KEY_BIO));
            }
            if (user.getString(ParseConstants.KEY_LOCATION) != null) {
                mLocationTextView.setText(user.getString(ParseConstants.KEY_LOCATION));
            }
            mEmailTextView.setText(user.getEmail());
            mPhoneTextView.setText(user.getString(ParseConstants.KEY_PHONE));
        }

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
        else if (position == 1) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.title_activity_my_account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Inflate the menu; this adds items to the action bar if it is present.if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            Intent intent = getIntent();
            String objectId = intent.getStringExtra(ParseConstants.KEY_USER_ID);
            if (objectId.equals(ParseUser.getCurrentUser().getObjectId())) {
                getMenuInflater().inflate(R.menu.menu_my_account, menu);
                restoreActionBar();
                return true;
            }
            else {
                restoreActionBar();
                return true;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_edit_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
