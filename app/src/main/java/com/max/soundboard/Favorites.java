package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;


class Favorites extends Group {
    public static final String NAME = "Favorites";

    public Favorites(Context context) {
        super(NAME, context);
        update();
    }

    public void update() {
        mSounds.clear();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, 0);
        for (String soundName : sharedPreferences.getStringSet(NAME, new HashSet<String>())) {
            Sound sound = SoundManager.getSoundByName(soundName);
            if (sound != null)
                mSounds.add(sound);
        }
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
