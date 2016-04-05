package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


class Favorites extends Group {
    public static final String NAME = "Favorites";
    private static RecyclerViewAdapter mFavoritesAdapter;

    public Favorites(Context context) {
        super(NAME, context);
        addAllValidSounds(getFavoritesFromSharedPreferences());
    }

    public void remove(Sound sound, boolean favoritesTab) {
        if (!contains(sound)) {
            Log.e(NAME, "Sound is not in the list of favorites!");
            return;
        }
        int index = mSounds.indexOf(sound);
        mSounds.remove(index);
        updateSharedPreferences();

        // Notify the favorites recyclerview adapter
        getFavoritesAdapter().notifyItemRemoved(index);

        // Update the tab which contains the original sound if we are in favorites tab
        if (favoritesTab) {
            String groupName = sound.getGroup().getName();
            ViewPagerAdapter adapter = ((SoundActivity) mContext).getViewPagerAdapter();
            RecyclerViewFragment origFragment = (RecyclerViewFragment) adapter.getFragment(groupName);
            origFragment.getAdapter().updateItem(sound);
        }
    }

    public void add(Sound sound) {
        if (contains(sound)) {
            Log.e(NAME, "Sound is already in the list of favorites!");
            return;
        }
        mSounds.add(sound);
        updateSharedPreferences();

        // Notify the favorites recyclerview adapter
        getFavoritesAdapter().notifyItemInserted(mSounds.size() - 1);
    }

    private Set<String> getFavoritesFromSharedPreferences() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, 0);
        return sharedPreferences.getStringSet(NAME, new HashSet<String>());
    }

    private void addAllValidSounds(Set<String> set) {
        for (String soundName : set) {
            Sound sound = SoundManager.getSoundByName(soundName);
            if (sound != null)
                mSounds.add(sound);
        }
    }

    private RecyclerViewAdapter getFavoritesAdapter() {
        if (mFavoritesAdapter == null) {
            ViewPagerAdapter adapter = ((SoundActivity) mContext).getViewPagerAdapter();
            RecyclerViewFragment favFragment = (RecyclerViewFragment) adapter.getFragment(NAME);
            mFavoritesAdapter = favFragment.getAdapter();
        }
        return mFavoritesAdapter;
    }

    private void updateSharedPreferences() {
        // Create a new hashset of the favorized sounds
        HashSet<String> set = new HashSet<>();
        for (Sound sound : mSounds)
            set.add(sound.getName());
        // Update the SharedPreferences with this new string set
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, 0).edit();
        editor.clear();
        editor.putStringSet(NAME, set);
        editor.apply();
    }
}
