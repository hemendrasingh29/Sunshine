package com.example.zendynamix.sunshine;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.zendynamix.sunshine.sync.SunshineSyncAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);


        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp-land). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            Log.e(LOG_TAG, "Error closing stream" + mTwoPane);
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
            MainActivityFragment forecastFragment =  ((MainActivityFragment)getSupportFragmentManager()
                                   .findFragmentById(R.id.fragment));
                    forecastFragment.setUseTodayLayout(!mTwoPane);
           // SunshineSyncAdapter.initializeSyncAdapter(this);

        }
        //  getSupportFragmentManager().beginTransaction().add(R.id.fragment, new MainActivityFragment(), FORECASTFRAGMENT_TAG)
        //         .commit();
        // mLocation = "94043";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));

        }
        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            MainActivityFragment ff = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (null != ff) {
                ff.onLocationChanged();
            }


            mLocation = location;

        }

    }
}




