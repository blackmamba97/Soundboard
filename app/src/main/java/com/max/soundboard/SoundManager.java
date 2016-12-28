package com.max.soundboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


class SoundManager {
    public static final String SOUND_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/Soundboard";
    private final ArrayList<SoundGroup> mSoundGroups = new ArrayList<>();
    private static final String TAG = "Soundmanager";
    private final Favorites mFavorites;

    public SoundManager(Context context) {
        // If the sound directory does not exist we create it
        if (!new File(SOUND_DIRECTORY).mkdir()) {
            // Setup sound group list
            mSoundGroups.clear();
            for (String groupName : FileManager.getSubdirectories(SOUND_DIRECTORY)) {
                // We cannot handle a group of sounds named "Favorites"
                if (!groupName.equals(Favorites.NAME)) {
                    SoundGroup soundGroup = new SoundGroup(groupName);
                    if (soundGroup.getSize() > 0) {
                        mSoundGroups.add(soundGroup);
                    }
                }
            }
        }
        mFavorites = new Favorites(context, this);
    }

    public Group getSoundGroupByName(String groupName) {
        if (groupName.equals(Favorites.NAME)) {
            return mFavorites;
        }
        for (SoundGroup soundGroup : mSoundGroups) {
            if (soundGroup.getName().equals(groupName)) {
                return soundGroup;
            }
        }
        Log.e(TAG, "Sound group for \"" + groupName + "\" does not exist!");
        return null;
    }

    public Sound getSoundByName(String name) {
        for (SoundGroup soundGroup : mSoundGroups) {
            for (Sound sound : soundGroup) {
                if (sound.getName().equals(name)) {
                    return sound;
                }
            }
        }
        Log.e(TAG, "Sound \"" + name + "\" does not exist!");
        return null;
    }

    public ArrayList<SoundGroup> getGroups() {
        return mSoundGroups;
    }

    public Favorites getFavorites() {
        return mFavorites;
    }
}
