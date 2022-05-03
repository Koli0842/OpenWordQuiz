package com.koli.openquiz.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.koli.openquiz.R;
import com.koli.openquiz.sync.view.login.LoginActivity;
import com.koli.openquiz.view.settings.SettingsActivity;
import com.koli.openquiz.view.statistics.StatisticsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_quiz));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean isFragment = true;
        int id = item.getItemId();

        if (id == R.id.nav_quiz) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new com.koli.openquiz.view.quiz.DictionarySelectionFragment()).commit();
            setTitle("Quiz");
        } else if(id == R.id.nav_dictionary) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new com.koli.openquiz.view.dictionary.DictionarySelectionFragment()).commit();
            setTitle("Dictionaries");
        } else if (id == R.id.nav_statistics) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new StatisticsFragment()).commit();
            setTitle("Statistics");
        } else if (id == R.id.nav_settings) {
            isFragment = false;
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_login) {
            isFragment = false;
            startActivity(new Intent(this, LoginActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return isFragment;
    }
}
