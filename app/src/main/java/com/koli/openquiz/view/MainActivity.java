package com.koli.openquiz.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.koli.openquiz.R;
import com.koli.openquiz.sync.view.login.LoginActivity;
import com.koli.openquiz.view.dictionary.DictionarySelectionFragment;
import com.koli.openquiz.view.quiz.QuizSelectionFragment;
import com.koli.openquiz.view.settings.SettingsActivity;
import com.koli.openquiz.view.statistics.StatisticsFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_dictionary, false);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

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

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean isFragment = true;

        switch (item.getItemId()) {
            case R.id.nav_quiz -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new QuizSelectionFragment()).commit();
                setTitle("Quiz");
            }
            case R.id.nav_dictionary -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DictionarySelectionFragment()).commit();
                setTitle("Dictionaries");
            }
            case R.id.nav_statistics -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StatisticsFragment()).commit();
                setTitle("Statistics");
            }
            case R.id.nav_settings -> {
                isFragment = false;
                startActivity(new Intent(this, SettingsActivity.class));
            }
            case R.id.nav_login -> {
                isFragment = false;
                startActivity(new Intent(this, LoginActivity.class));
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return isFragment;
    }
}
