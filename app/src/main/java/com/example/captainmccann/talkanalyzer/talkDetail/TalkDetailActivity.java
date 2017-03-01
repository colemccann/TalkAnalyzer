package com.example.captainmccann.talkanalyzer.talkDetail;

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
import android.widget.Toast;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.stats.StatsActivity;
import com.example.captainmccann.talkanalyzer.talks.TalksActvity;

public class TalkDetailActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    public static final String TALK_DETAIL_ID = "com.example.captainmccann.talkanalyzer.TALK_DETAIL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.talk_detail_toolbar);
        toolbar.setTitle(R.string.talk_details);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_drawer_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.talk_detail_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.talk_detail_nav_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        // Get the talk ID
        //// TODO: 1/18/2017 handle if id does end up as -1 (in talk detail fragment)
        int talkId = getIntent().getIntExtra(TALK_DETAIL_ID, -1);

        initFragment(TalkDetailFragment.newInstance(talkId));

    }

    private void initFragment(Fragment newInstance) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.talk_detail_fragment_frame, newInstance);
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
                        Toast.makeText(TalkDetailActivity.this, "Settings have not been initialized!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.stats:
                        startActivity(new Intent(TalkDetailActivity.this, StatsActivity.class));
                        break;

                    case R.id.home:
                        TalkDetailActivity.this.finish();
                        break;

                    default:
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
