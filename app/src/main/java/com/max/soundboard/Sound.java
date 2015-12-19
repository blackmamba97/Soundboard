package com.max.soundboard;

import java.io.File;


class Sound {
    private final String name;
    private final File directory;
    private boolean isPlaying = false;

    public Sound(String name, File directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
