package com.max.soundboard;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;


public class SoundActivity extends AppCompatActivity {
    private static final String EXTERNAL_STORAGE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add tabs to the tab layout
        checkPermissionAndSetupTabs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                setupTabs();
            } else {
                // Exit app if the user does not grant the permissions
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermissionAndSetupTabs() {
        // Ask for permission to access the groups on the internal storage
        int permissionsGranted = ActivityCompat.checkSelfPermission(this, EXTERNAL_STORAGE_PERM);
        if (permissionsGranted != PackageManager.PERMISSION_GRANTED) {
            // Permissions need to be granted, so we ask the user
            requestPermission();
        } else {
            // Permissions are already granted
            setupTabs();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, EXTERNAL_STORAGE_PERM)) {
            showRequestPermissionDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{EXTERNAL_STORAGE_PERM},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void showRequestPermissionDialog() {
        new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.storage_access_dialog_title))
                .setMessage(getString(R.string.storage_access_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SoundActivity.this,
                                new String[]{EXTERNAL_STORAGE_PERM}, PERMISSION_REQUEST_CODE);
                    }
                }).show();
    }

    private void setupTabs() {
        // Add favorites and sound groups to the view pager adapter
        addTab(Favorites.NAME);
        addSoundGroupTabs();

        // Adapter is setup and can be attached to the view pager
        mViewPager.setAdapter(mViewPagerAdapter);

        // Setup tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListener());
    }

    private void addSoundGroupTabs() {
        // Create a new tab for each group
        SoundManager soundManager = SoundManager.getInstance(getApplicationContext());
        ArrayList<SoundGroup> soundGroups = soundManager.getGroups();
        boolean areSoundsPresent = false;

        if (soundGroups.isEmpty()) {
            showNoSoundsFoundDialog();
            return;
        }
        for (SoundGroup soundGroup : soundGroups) {
            String directory = soundGroup.getDirectory();
            ArrayList<String> soundFileNames = FileManager.getSoundFileNames(directory);

            if (!soundFileNames.isEmpty()) {
                addTab(soundGroup.getName());
                areSoundsPresent = true;
            }
        }
        if (!areSoundsPresent)
            showNoSoundsFoundDialog();
    }

    private void showNoSoundsFoundDialog() {
        // Close the app if no sounds are found
        new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.no_sounds_found_dialog_title))
                .setMessage(getString(R.string.no_sounds_found_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    private void addTab(String groupName) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();

        // Pass the group name as an argument
        Bundle args = new Bundle();
        args.putString(RecyclerViewFragment.GROUP_NAME, groupName);
        fragment.setArguments(args);

        // Add the fragment to the ViewPagerAdapter
        mViewPagerAdapter.addFrag(fragment, groupName);
    }

    private class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // Set the current item in the ViewPager
            int tabIndex = tab.getPosition();
            mViewPager.setCurrentItem(tabIndex);

            // If the favorites tab is selected we update the list of favorites in the adapter
            if (tab.getPosition() == 0) {
                ViewPagerAdapter pagerAdapter = (ViewPagerAdapter) mViewPager.getAdapter();
                RecyclerViewFragment fragment = (RecyclerViewFragment) pagerAdapter.getItem(tabIndex);
                fragment.getAdapter().updateSounds();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // Stop mediaplayer when an sound is playing and tabs are switched
            if (SoundPlayer.getMediaPlayer() != null) {
                SoundPlayer.reset();
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    }
}