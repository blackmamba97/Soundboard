package com.max.soundboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;


public class SoundActivity extends AppCompatActivity {
    private static final String EXTERNAL_STORAGE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener());

        mSoundManager = new SoundManager(this);
        checkPermissionAndSetupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshTabs();
        }
        return true;
    }

    private void refreshTabs() {
        mViewPager.setCurrentItem(0, false);

        // Remove all fragments except the favorites.
        for (int i = mViewPagerAdapter.getCount() - 1; i > 0; i--) {
            mViewPagerAdapter.removeFragment(i);
        }

        // Refresh and add all fragments except the favorites.
        setupTabs(false);

        // Refresh the favorites fragment.
        ((RecyclerViewFragment) mViewPagerAdapter.getFragment(SoundManager.FAVORITES))
                .notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                setupTabs(true);
            } else {
                // Exit app if the user does not grant the permissions.
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        return mViewPagerAdapter;
    }

    private void checkPermissionAndSetupTabs() {
        // Ask for permission to access the groups on the internal storage.
        if (ActivityCompat.checkSelfPermission(this, EXTERNAL_STORAGE_PERM)
                == PackageManager.PERMISSION_GRANTED) {
            // Permissions are already granted.
            setupTabs(true);
        } else {
            // Permissions need to be granted, so we ask the user.
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, EXTERNAL_STORAGE_PERM)) {
            // Show rationale permission request dialog.
            new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                    .setTitle(getString(R.string.storage_access_dialog_title))
                    .setMessage(getString(R.string.storage_access_dialog_message))
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> ActivityCompat.requestPermissions(this,
                                    new String[]{EXTERNAL_STORAGE_PERM}, PERMISSION_REQUEST_CODE))
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{EXTERNAL_STORAGE_PERM},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void setupTabs(boolean addFavorites) {
        mSoundManager.refreshGroups();
        Set<String> groupNames = mSoundManager.getGroupNames();

        // Add favorites and sound groups to the view pager adapter.
        if (groupNames.size() <= 1) {
            // Close the app if no sounds are found.
            new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                    .setTitle(getString(R.string.no_sounds_found_dialog_title))
                    .setMessage(getString(R.string.no_sounds_found_dialog_message))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                    .show();
        } else {
            for (String groupName : groupNames) {
                if (addFavorites || !groupName.equals(SoundManager.FAVORITES)) {
                    addTab(groupName);
                }
            }
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }

    private void addTab(String groupName) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();

        // Pass the group name as an argument.
        Bundle args = new Bundle();
        args.putString(RecyclerViewFragment.GROUP_NAME, groupName);
        fragment.setArguments(args);

        mViewPagerAdapter.addFrag(fragment, groupName);
    }

    public SoundManager getSoundManager() {
        return mSoundManager;
    }

    private class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // Set the current item in the ViewPager.
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // Stop media player when an sound is playing and tabs are switched.
            SoundPlayer.reset(SoundActivity.this);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }
}