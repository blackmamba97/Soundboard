package com.max.soundboard;

import java.io.File;


class Sound {
    private final String name;
    private final File directory;
    private final String groupName;
    private boolean isPlaying = false;

    public Sound(String name, File directory, String groupName) {
        this.name = name;
        this.directory = directory;
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Sound && name.equals(((Sound) obj).name)
                && groupName.equals(((Sound) obj).groupName);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + groupName.hashCode();
    }

    @Override
    public String toString() {
        return groupName + File.separator + name;
    }
}
