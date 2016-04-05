package com.max.soundboard;

import android.content.Context;

import java.io.File;


class SoundGroup extends Group {
    public SoundGroup(String groupName, Context context) {
        super(groupName, context);
        String directory = SoundManager.SOUND_DIRECTORY + "/" + groupName;
        for (String soundName : FileManager.getSoundFileNames(directory)) {
            File soundDir = new File(directory + "/" + soundName);
            mSounds.add(new Sound(soundName, soundDir));
        }
    }

    @Override
    public int removeSoundFromFavorites(Sound sound) {
        SoundManager.getFavorites().removeSoundFromFavorites(sound);
        return -1;
    }
}
