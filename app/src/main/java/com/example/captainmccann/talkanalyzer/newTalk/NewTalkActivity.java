package com.example.captainmccann.talkanalyzer.newTalk;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.captainmccann.talkanalyzer.R;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class NewTalkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_talk);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_talk_toolbar);
        toolbar.setTitle(R.string.new_talk);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            initFragment(NewTalkFragment1.newInstance());
        }
    }

    private void initFragment(Fragment newInstance) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.new_talk_fragment_frame, newInstance);
        transaction.commit();
    }

}
