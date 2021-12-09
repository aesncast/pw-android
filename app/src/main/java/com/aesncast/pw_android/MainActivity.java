package com.aesncast.pw_android;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private int current_fragment = 0;

    public static Handler handler;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    private MainTabsFragment mainFragment;
    private SettingsFragment settingsFragment;

    private PwPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new PwPreferences(this);
        setContentView(R.layout.activity_main);
        handler = new Handler();

        setupPwfile();
        setupFragments();
        setupTopbar();
        setupDrawer();
        setupDrawerToggle();

        if (savedInstanceState == null) {
            if (preferences.getStartOnPasswordGenerator())
                navigateToPasswordGenerator();
            else
                navigateTo(R.id.nav_home);
        }
    }

    private void setupPwfile()
    {
        if (PwfileSingleton.instance == null) {
            PwfileSingleton.instance = new PwfileController(preferences);
            PwfileSingleton.instance.load();
        }
    }

    private void setupFragments()
    {
        this.mainFragment = new MainTabsFragment();
        this.settingsFragment = new SettingsFragment();
    }

    private void setupTopbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,  R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
        updateTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        /*
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
         */

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            menuItem -> {
                selectDrawerItem(menuItem);
                return true;
            });
    }

    private void updateTitle()
    {
        if (current_fragment == R.id.nav_home)
            setTitle(getString(R.string.home));
        else if (current_fragment == R.id.nav_password)
            setTitle(getString(R.string.generate_password));
        else if (current_fragment == R.id.nav_sequence)
            setTitle(getString(R.string.sequence_editor));
        else if (current_fragment == R.id.nav_settings)
            setTitle(getString(R.string.settings));
    }

    private void setActiveFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        handler.post(() -> fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit());
    }

    public void navigateToPasswordGenerator()
    {
        navigateToPasswordGenerator("", "", PwfileSingleton.instance.get().default_sequence_name);
    }

    public void navigateToPasswordGenerator(String domain, String user, String sequenceName)
    {
        if (this.current_fragment == R.id.nav_password)
            return;

        Fragment fragment = PasswordFragment.newInstance(domain, user, sequenceName);
        this.current_fragment = R.id.nav_password;
        setActiveFragment(fragment);
        updateTitle();
    }

    public void navigateToSequenceEditor()
    {
        navigateToSequenceEditor("");
    }

    public void navigateToSequenceEditor(String sequenceName)
    {
        if (this.current_fragment == R.id.nav_sequence)
            return;

        Fragment fragment = SequenceEditorFragment.newInstance(sequenceName);
        this.current_fragment = R.id.nav_sequence;
        setActiveFragment(fragment);
        updateTitle();
    }

    private void navigateTo(int id)
    {
        Fragment fragment = null;

        if (id == R.id.nav_home)
            fragment = mainFragment;
        else if (id == R.id.nav_password)
            fragment = PasswordFragment.newInstance("", "", PwfileSingleton.instance.get().default_sequence_name);
        else if (id == R.id.nav_sequence)
            fragment = SequenceEditorFragment.newInstance("");
        else if (id == R.id.nav_settings)
            fragment = settingsFragment;

        this.current_fragment = id;
        setActiveFragment(fragment);
        updateTitle();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Insert the fragment by replacing any existing fragment
        int itemId = menuItem.getItemId();
        navigateTo(itemId);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed()
    {
        if (current_fragment == R.id.nav_home)
            finish();
        else
            navigateTo(R.id.nav_home);
    }
}