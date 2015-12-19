package com.max.soundboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


class SoundManager {
    public static final String SOUND_DIRECTORY = Environment.getExternalStorageDirectory() + "/Soundboard";
    private static SoundManager instance;
    private final ArrayList<SoundGroup> mSoundGroups = new ArrayList<>();
    private final Favorites mFavorites;
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;

    private SoundManager(Context context) {
        mContext = context;
        mFavorites = new Favorites(context);
        new File(SOUND_DIRECTORY).mkdir();
        updateGroups();
    }

    public static SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public SoundGroup getSoundGroupByName(String groupName) {
        for (SoundGroup soundGroup : mSoundGroups) {
            if (soundGroup.getName().equals(groupName))
                return soundGroup;
        }
        Log.e(TAG, "Sound group for \"" + groupName + "\" does not exist!");
        return null;
    }

    public String getGroupNameBySound(Sound sound) {
        for (SoundGroup soundGroup : mSoundGroups) {
            if (soundGroup.contains(sound))
                return soundGroup.getName();
        }
        Log.e(TAG, "Sound group for \"" + sound.getName() + "\" does not exist!");
        return null;
    }

    public File getSoundPath(String soundName) {
        for (SoundGroup soundGroup : mSoundGroups) {
            String path = SOUND_DIRECTORY + "/" + soundGroup.getName();
            if (FileManager.getSoundFileNames(path).contains(soundName))
                return new File(path + "/" + soundName);
        }
        Log.e(TAG, "Soundpath for \"" + soundName + "\" does not exist!");
        return null;
    }

    public ArrayList<SoundGroup> getGroups() {
        return mSoundGroups;
    }

    private void updateGroups() {
        mSoundGroups.clear();
        for (String groupName : FileManager.getSubdirectories(SOUND_DIRECTORY))
            mSoundGroups.add(new SoundGroup(groupName, mContext));
    }

    public Favorites getFavorites() {
        return mFavorites;
    }
}
