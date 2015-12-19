package com.max.soundboard;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;


class SoundGroup extends Group {
    private final ArrayList<Sound> mSounds = new ArrayList<>();
    private final String mName;
    private final String mDirectory;
    private final Context mContext;

    public SoundGroup(String groupName, Context context) {
        mName = groupName;
        mDirectory = SoundManager.SOUND_DIRECTORY + "/" + groupName;
        mContext = context;
        updateSounds();
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public ArrayList<Sound> getSounds() {
        return mSounds;
    }

    @Override
    public void updateSounds() {
        mSounds.clear();
        for (String soundName : FileManager.getSoundFileNames(mDirectory)) {
            File soundDir = new File(mDirectory + "/" + soundName);
            mSounds.add(new Sound(soundName, soundDir));
        }
    }

    @Override
    public int removeSound(Sound sound) {
        SoundManager.getInstance(mContext).getFavorites().removeSound(sound);
        return -1;
    }

    public String getDirectory() {
        return mDirectory;
    }

    public boolean contains(Sound sound) {
        for (Sound sound1 : mSounds)
            if (sound1.getName().equals(sound.getName()))
                return true;
        return false;
    }
}
