package com.max.soundboard;

import java.io.File;
import java.util.ArrayList;


class FileManager {
    private FileManager() {}

    public static ArrayList<String> getSubdirectories(String path) {
        ArrayList<String> subdirectories = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    subdirectories.add(file.getName());
                }
            }
        }
        return subdirectories;
    }

    public static ArrayList<String> getSoundFileNames(String path) {
        ArrayList<String> soundFiles = new ArrayList<>();

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
