package com.sousheelvunnam.scrimmage.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;
import com.sousheelvunnam.scrimmage.R;
import com.sousheelvunnam.scrimmage.adapters.NavDrawerListAdapter;
import com.sousheelvunnam.scrimmage.model.NavDrawerItem;
import com.sousheelvunnam.scrimmage.ui.GameActivity;
import com.sousheelvunnam.scrimmage.ui.MyAccountActivity;
import com.sousheelvunnam.scrimmage.ui.MyActivity;

import java.util.ArrayList;

/**
 * Created by Sousheel on 12/19/2014.
 */
public class NavDrawer {

    private String[] mDrawerLabels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter mNavDrawerListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    public void navDrawerOnCreate(final Context context, View view) {
        mDrawerTitle = "Scrimmage";
        mDrawerLabels = context.getResources().getStringArray(R.array.nav_drawer_labels);
        navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) view.findViewById(R.id.left_drawer);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(mDrawerLabels[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(mDrawerLabels[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(mDrawerLabels[2], navMenuIcons.getResourceId(2, -1)));
        navMenuIcons.recycle();
        mNavDrawerListAdapter = new NavDrawerListAdapter(context.getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mNavDrawerListAdapter);

        // enabling action bar app icon and behaving it as toggle button
        /*activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);*/

        /*mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_launcher, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                activity.getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                activity.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                activity.getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                activity.invalidateOptionsMenu();
            }
        };*/
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (context.getClass() == MyActivity.class) {
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            Intent intent = new Intent(context, MyActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                    case 1:
                        if (context.getClass() == GameActivity.class) {
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            Intent intent = new Intent(context, GameActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                    case 2:
                        if (context.getClass() == MyAccountActivity.class) {
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            Intent intent = new Intent(context, MyAccountActivity.class);
                            intent.putExtra(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
                            context.startActivity(intent);
                        }
                        break;
                }
            }
        });
    }
    /*public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/
}
