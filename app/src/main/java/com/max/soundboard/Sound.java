package com.max.soundboard;

import java.io.File;


class Sound {
    private final String name;
    private final File directory;
    private final SoundGroup group;
    private boolean isPlaying = false;

    public Sound(String name, File directory, SoundGroup group) {
        this.name = name;
        this.directory = directory;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public SoundGroup getGroup() {
        return group;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
