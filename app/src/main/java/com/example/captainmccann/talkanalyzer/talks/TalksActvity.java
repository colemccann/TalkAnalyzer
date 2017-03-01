package com.example.captainmccann.talkanalyzer.talks;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.stats.StatsActivity;

public class TalksActvity extends AppCompatActivity {

    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talks);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.talks_toolbar);
        toolbar.setTitle(R.string.title_activity_talks);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_drawer_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.talks_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.talks_nav_view);
        if (navigationView != null){
            setupNavigationDrawerContent(navigationView);
        }

        if (null == savedInstanceState) {
            initFragment(TalksFragment.newInstance());
        }

    }

    private void initFragment(Fragment newInstance) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.talks_fragment_frame, newInstance);
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
                    case R.id.settings:
                        Toast.makeText(TalksActvity.this, "Settings have not been initialized!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.stats:
                        startActivity(new Intent(TalksActvity.this, StatsActivity.class));
                        break;

                    case R.id.home:
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

}
