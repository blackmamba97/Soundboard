package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


class Favorites extends Group {
    public static final String NAME = "Favorites";

    public Favorites(Context context) {
        super(NAME, context);
        addAllValidSounds(getFavoritesFromSharedPreferences());
    }

    public boolean updateIfRequired() {
        HashSet<String> newFavorites = new HashSet<>();
        HashSet<String> favorites = new HashSet<>();

        newFavorites.addAll(getFavoritesFromSharedPreferences());
        for (Sound sound : mSounds)
            favorites.add(sound.getName());

        // Check if favorites have changed
        if (favorites.equals(newFavorites))
            return false;

        // Update favorites
        mSounds.clear();
        addAllValidSounds(newFavorites);
        return true;
    }

    @Override
    public int removeSoundFromFavorites(Sound sound) {
        if (!contains(sound)) {
            Log.e(NAME, "Sound is not in the list of favorites!");
            return -1;
        }
        int index = mSounds.indexOf(sound);
        mSounds.remove(index);
        updateSharedPreferences();

        // Update the tab which contains the original sound
        String groupName = sound.getGroup().getName();
        ViewPagerAdapter adapter = ((SoundActivity) mContext).getViewPagerAdapter();
        RecyclerViewFragment fragment = (RecyclerViewFragment) adapter.getFragment(groupName);
        fragment.getAdapter().updateItem(sound);

        // Return the index of the sound to remove it from the favorites recyclerview
        return index;
    }

    public void addSoundToFavorites(Sound sound) {
        if (contains(sound)) {
            Log.e(NAME, "Sound is already in the list of favorites!");
            return;
        }
        mSounds.add(sound);
        updateSharedPreferences();
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
