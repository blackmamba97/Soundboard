package com.max.soundboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


class FileManager {
    private FileManager() {
    }

    public static List<String> getSubdirectories(String path) {
        List<String> subdirectories = new ArrayList<>();

        for (File file : new File(path).listFiles()) {
            if (file.isDirectory()) {
                subdirectories.add(file.getName());
            }
        }
        return subdirectories;
    }

    public static List<String> getSoundFileNames(String path) {
        List<String> soundFiles = new ArrayList<>();

        for (File file : new File(path).listFiles()) {
            if (file.isFile() && isSoundFileName(file.getName())) {
                soundFiles.add(file.getName());
            }
        }
        return soundFiles;
    }

    private static boolean isSoundFileName(String filename) {
        return filename.endsWith(".mp3") || filename.endsWith(".wav");
    }
}
