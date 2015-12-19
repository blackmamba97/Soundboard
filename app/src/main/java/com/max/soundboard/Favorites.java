package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;


class Favorites extends Group {
    private final String TAG = this.getClass().getSimpleName();
    public static final String NAME = "Favorites";
    private final ArrayList<Sound> mSounds = new ArrayList<>();
    private final Context mContext;

    public Favorites(Context context) {
        mContext = context;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ArrayList<Sound> getSounds() {
        return mSounds;
    }

    @Override
    public void updateSounds() {
        mSounds.clear();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, 0);
        for (String soundName : sharedPreferences.getStringSet(NAME, new HashSet<String>())) {
            File soundDir = SoundManager.getInstance(mContext).getSoundPath(soundName);
            if (soundDir != null)
                mSounds.add(new Sound(soundName, soundDir));
        }
    }

    @Override
    public int removeSound(Sound sound) {
        if (contains(sound)) {
            // Get the index of the sound to removed
            int index = mSounds.indexOf(sound);
            // Create a new hash set of all favorites
            HashSet<String> set = new HashSet<>();
            for (Sound favorite : mSounds)
                set.add(favorite.getName());
            // Remove the sound
            set.remove(sound.getName());
            updateSharedPreferneces(set);
            // Update mSounds to represent the new favorites
            updateSounds();
            // Return the index of the sound to remove it from the favorites recyclerview
            return index;
        } else {
            Log.e(TAG, "Sound is not in the list of favorites!");
            return -1;
        }
    }

    public void addSound(Sound sound) {
        if (!contains(sound)) {
            mSounds.add(sound);
            // Create a new hash set of all favorites and the added one
            HashSet<String> set = new HashSet<>();
            for (Sound favorite : mSounds)
                set.add(favorite.getName());
            updateSharedPreferneces(set);
        } else {
            Log.e(TAG, "Sound is already in the list of favorites!");
        }
    }

    public boolean contains(Sound sound) {
        for (Sound favorite : mSounds)
            if (favorite.getName().equals(sound.getName()))
                return true;
        return false;
    }

    private void updateSharedPreferneces(HashSet<String> set) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, 0).edit();
        editor.clear();
        editor.putStringSet(NAME, set);
        editor.apply();
    }
}
