package com.koli.openvocab.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.koli.openvocab.R;
import com.koli.openvocab.sync.view.login.LoginActivity;
import com.koli.openvocab.view.dictionary.DictionarySelectionFragment;
import com.koli.openvocab.view.quiz.QuizSelectionFragment;
import com.koli.openvocab.view.settings.SettingsFragment;
import com.koli.openvocab.view.statistics.StatisticsFragment;

import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private SharedPreferences preferences;
    private int activePage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_quiz, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_quiz_types, false);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawer_layout);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (activePage == R.id.nav_quiz) {
            getMenuInflater().inflate(R.menu.quiz_preferences, menu);
            IntStream.range(0, menu.size()).mapToObj(menu::getItem).forEach(item ->
                item.setChecked(preferences.getBoolean(getPreferencesKey(item), false)));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean newValue = !item.isChecked();

        if (item.getItemId() == R.id.quiz_preferences_written) {
            preferences.edit().putBoolean(getPreferencesKey(item), newValue).apply();
        } else if (item.getItemId() == R.id.quiz_preferences_verbal) {
            preferences.edit().putBoolean(getPreferencesKey(item), newValue).apply();
        }

        item.setChecked(newValue);
        return super.onOptionsItemSelected(item);
    }

    private String getPreferencesKey(MenuItem item) {
        if (item.getItemId() == R.id.quiz_preferences_written) {
            return getResources().getString(R.string.quiz_textual_enabled_key);
        } else if (item.getItemId() == R.id.quiz_preferences_verbal) {
            return getResources().getString(R.string.quiz_verbal_enabled_key);
        }
        return null;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        boolean isFragment = true;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_quiz) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new QuizSelectionFragment()).commit();
            activePage = itemId;
            invalidateOptionsMenu();
            setTitle("Quiz");
        } else if (itemId == R.id.nav_dictionary) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DictionarySelectionFragment()).commit();
            activePage = itemId;
            invalidateOptionsMenu();
            setTitle("Dictionaries");
        } else if (itemId == R.id.nav_statistics) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StatisticsFragment()).commit();
            activePage = itemId;
            invalidateOptionsMenu();
            setTitle("Statistics");
        } else if (itemId == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
            activePage = itemId;
            invalidateOptionsMenu();
            setTitle("Settings");
        } else if (itemId == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return isFragment;
    }
}
