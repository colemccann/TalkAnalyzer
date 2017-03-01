package com.example.captainmccann.talkanalyzer.stats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.captainmccann.talkanalyzer.R;

public class StatsActivity extends AppCompatActivity {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.stats_toolbar);
        toolbar.setTitle(R.string.statistics);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_drawer_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.stats_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.stats_nav_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        if (null == savedInstanceState) {
            initFragment(StatsFragment.newInstance());
        }
    }

    private void initFragment(Fragment newInstance) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.stats_fragment_frame, newInstance);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //// TODO: 1/18/2017 setup settings and stats behavior
                    case R.id.settings:
                        break;

                    case R.id.stats:
                        break;

                    case R.id.home:
                        StatsActivity.this.finish();

                    default:
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
