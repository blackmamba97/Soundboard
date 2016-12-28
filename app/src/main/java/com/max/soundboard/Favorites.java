package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


class Favorites extends Group {
    public static final String NAME = "Favorites";
    private RecyclerViewAdapter mFavoritesAdapter;
    private final SharedPreferences mSharedPreferences;

    public Favorites(Context context, SoundManager soundManager) {
        super(NAME);
        mSharedPreferences = context.getSharedPreferences(NAME, 0);

        Set<String> savedFavorites = mSharedPreferences.getStringSet(NAME, new HashSet<String>());
        for (String soundName : savedFavorites) {
            Sound sound = soundManager.getSoundByName(soundName);
            if (sound != null) {
                mSounds.add(sound);
            }
        }
    }

    public void remove(Sound sound) {
        int index = indexOf(sound);

        if (index < 0) {
            Log.e(NAME, "Sound is not in the list of favorites!");
        } else {
            mSounds.remove(index);
            updateSharedPreferences();
            mFavoritesAdapter.notifyItemRemoved(index);
        }
    }

    public void add(Sound sound) {
        if (contains(sound)) {
            Log.e(NAME, "Sound is already in the list of favorites!");
        } else {
            mSounds.add(sound);
            updateSharedPreferences();
            mFavoritesAdapter.notifyItemInserted(mSounds.size() - 1);
        }
    }

    public void setFavoritesAdapter(RecyclerViewAdapter favoritesAdapter) {
        mFavoritesAdapter = favoritesAdapter;
    }

    private void updateSharedPreferences() {
        // Create a new hash set of the favored sounds
        HashSet<String> set = new HashSet<>();
        for (Sound sound : mSounds) {
            set.add(sound.getName());
        }

        // Update the SharedPreferences with this new string set
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.putStringSet(NAME, set);
        editor.apply();
    }
}
