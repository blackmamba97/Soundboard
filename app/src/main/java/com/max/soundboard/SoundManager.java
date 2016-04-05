package com.max.soundboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


class SoundManager {
    public static final String SOUND_DIRECTORY = Environment.getExternalStorageDirectory() + "/Soundboard";
    private static final ArrayList<SoundGroup> mSoundGroups = new ArrayList<>();
    private static final String TAG = "Soundmanager";
    private static Favorites mFavorites;

    public static Favorites setupFavorites(Context context) {
        // If the sound directory does not exist we create it
        if (!new File(SOUND_DIRECTORY).mkdir()) {
            // Setup sound group list
            mSoundGroups.clear();
            for (String groupName : FileManager.getSubdirectories(SOUND_DIRECTORY)) {
                SoundGroup soundGroup = new SoundGroup(groupName, context);
                if (soundGroup.getSize() > 0)
                    mSoundGroups.add(soundGroup);
            }
        }
        return mFavorites = new Favorites(context);
    }

    public static SoundGroup getSoundGroupByName(String groupName) {
        for (SoundGroup soundGroup : mSoundGroups)
            if (soundGroup.getName().equals(groupName))
                return soundGroup;
        Log.e(TAG, "Sound group for \"" + groupName + "\" does not exist!");
        return null;
    }

    public static String getGroupNameBySound(Sound sound) {
        for (SoundGroup soundGroup : mSoundGroups)
            if (soundGroup.contains(sound))
                return soundGroup.getName();
        Log.e(TAG, "Sound group for \"" + sound.getName() + "\" does not exist!");
        return null;
    }

    public static Sound getSoundByName(String name) {
        for (SoundGroup soundGroup : mSoundGroups)
            for (Sound sound : soundGroup)
                if (sound.getName().equals(name))
                    return sound;
        Log.e(TAG, "Sound \"" + name + "\" does not exist!");
        return null;
    }

    public static ArrayList<SoundGroup> getGroups() {
        return mSoundGroups;
    }

    public static Favorites getFavorites() {
        return mFavorites;
    }
}
