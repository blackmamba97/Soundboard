package com.max.soundboard;

import java.io.File;


class SoundGroup extends Group {
    public SoundGroup(String groupName) {
        super(groupName);
        String directory = SoundManager.SOUND_DIRECTORY + "/" + groupName;
        for (String soundName : FileManager.getSoundFileNames(directory)) {
            File soundDir = new File(directory + "/" + soundName);
            mSounds.add(new Sound(soundName, soundDir, this));
        }
    }
}
