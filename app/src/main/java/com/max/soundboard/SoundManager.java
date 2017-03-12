package com.max.soundboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


class SoundManager {
    private static final String SOUND_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/Soundboard";
    public static final String FAVORITES = "Favorites";
    private static final String TAG = "Soundmanager";
    private static final String SEPARATOR = File.separator;
    private final Map<String, List<Sound>> mSoundGroups = new LinkedHashMap<>();
    private final SharedPreferences mSharedPreferences;
    private RecyclerViewAdapter mFavoritesAdapter;

    public SoundManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(FAVORITES, 0);
    }

    public void refreshGroups() {
        if (new File(SOUND_DIRECTORY).mkdir()) {
            // If the sound directory does not exist we create it and the activity shows a dialog
            // so the user can copy sounds into the directory.
            Log.e(TAG, "No directory named \"" + SOUND_DIRECTORY
                    + "\" was found on the internal storage!");
            mSoundGroups.clear();
        } else {
            Log.d(TAG, "Scanning \"" + SOUND_DIRECTORY + "\" for sounds...");

            // The favorites must be the first element of the map.
            List<Sound> favorites = mSoundGroups.getOrDefault(FAVORITES, new ArrayList<>());
            mSoundGroups.clear();
            mSoundGroups.put(FAVORITES, favorites);
            setupSoundGroups();

            // This method can only be called after all the other sound groups have been setup.
            refreshFavorites(favorites);
        }
    }

    private void setupSoundGroups() {
        for (String groupName : FileManager.getSubdirectories(SOUND_DIRECTORY)) {
            // We cannot handle a group of sounds named "Favorites".
            if (groupName.equals(FAVORITES)) {
                Log.e(TAG, "\"" + FAVORITES + "\" is not a valid sound group name!");
            } else {
                List<Sound> group = new ArrayList<>();
                String directory = SOUND_DIRECTORY + SEPARATOR + groupName;

                for (String soundName : FileManager.getSoundFileNames(directory)) {
                    File soundDir = new File(directory + SEPARATOR + soundName);
                    group.add(new Sound(soundName, soundDir, groupName));
                }

                if (group.isEmpty()) {
                    Log.w(TAG, "Sound group \"" + groupName + "\" does not contain any sounds!");
                } else {
                    Log.d(TAG, "Sound group \"" + groupName + "\" with " + group.size()
                            + " sound(s) was found.");
                    mSoundGroups.put(groupName, group);
                }
            }
        }
    }

    private void refreshFavorites(List<Sound> favorites) {
        favorites.clear();
        Set<String> savedFavorites = mSharedPreferences.getStringSet(FAVORITES, new HashSet<>());

        // Search for each saved favorite and add existing sounds to the list.
        for (String savedString : savedFavorites) {
            String[] tokens = savedString.split(SEPARATOR);
            Sound favoredSound = searchSound(tokens[0], tokens[1]);

            if (favoredSound != null) {
                favorites.add(favoredSound);
            }
        }
    }

    private Sound searchSound(String groupName, String soundName) {
        for (Sound sound : mSoundGroups.getOrDefault(groupName, new ArrayList<>())) {
            if (sound.getName().equals(soundName)) {
                Log.d(TAG, "\"" + sound + "\" was favored and is present.");
                return sound;
            }
        }
        Log.w(TAG, "\"" + groupName + SEPARATOR + soundName + "\" was favored but is not present!");
        return null;
    }

    public List<Sound> getSoundGroupByName(String groupName) {
        if (mSoundGroups.containsKey(groupName)) {
            return Collections.unmodifiableList(mSoundGroups.get(groupName));
        } else {
            Log.e(TAG, "Sound group for \"" + groupName + "\" does not exist!");
            return null;
        }
    }

    public Set<String> getGroupNames() {
        return Collections.unmodifiableSet(mSoundGroups.keySet());
    }

    public boolean isFavored(Sound sound) {
        return mSoundGroups.get(FAVORITES).contains(sound);
    }

    public void notifySoundChanged(Sound sound) {
        if (isFavored(sound)) {
            mFavoritesAdapter.notifyItemChanged(mSoundGroups.get(FAVORITES).indexOf(sound));
        }
    }

    public void setFavoritesAdapter(RecyclerViewAdapter favoritesAdapter) {
        mFavoritesAdapter = favoritesAdapter;
    }

    public void removeFromFavorites(Sound sound) {
        List<Sound> favorites = mSoundGroups.get(FAVORITES);
        int index = favorites.indexOf(sound);

        if (index < 0) {
            Log.e(TAG, "\"" + sound + "\" is not in the list of favorites!");
        } else {
            Log.d(TAG, "\"" + sound + "\" was removed from the list of favorites.");
            favorites.remove(index);
            updateSharedPreferences();
            mFavoritesAdapter.notifyItemRemoved(index);
        }
    }

    public void addToFavorites(Sound sound) {
        List<Sound> favorites = mSoundGroups.get(FAVORITES);

        if (favorites.contains(sound)) {
            Log.e(TAG, "\"" + sound + "\" is already in the list of favorites!");
        } else {
            Log.d(TAG, "\"" + sound + "\" was added to the list of favorites.");
            favorites.add(sound);
            updateSharedPreferences();
            mFavoritesAdapter.notifyItemInserted(favorites.size() - 1);
        }
    }

    private void updateSharedPreferences() {
        // Create a new hash set of the favored sounds.
        HashSet<String> set = new HashSet<>();

        for (Sound sound : mSoundGroups.get(FAVORITES)) {
            set.add(sound.getGroupName() + SEPARATOR + sound.getName());
        }

        // Update the SharedPreferences with this new string set.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.putStringSet(FAVORITES, set);
        editor.apply();
    }
}
