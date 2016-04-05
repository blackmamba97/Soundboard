package com.max.soundboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;


abstract class Group implements Iterable<Sound> {
    protected final ArrayList<Sound> mSounds = new ArrayList<>();
    protected final String mName;
    protected final Context mContext;

    protected Group(String name, Context context) {
        mName = name;
        mContext = context;
    }

    public String getName() {
        return mName;
    }

    public Sound getSound(int position) {
        return mSounds.get(position);
    }

    public int indexOf(Sound sound) {
        return mSounds.indexOf(sound);
    }

    public int getSize() {
        return mSounds.size();
    }

    public boolean contains(Sound sound) {
        for (Sound s : mSounds)
            if (s.getName().equals(sound.getName()))
                return true;
        return false;
    }

    public Iterator<Sound> iterator() {
        return mSounds.iterator();
    }

    public abstract int removeSoundFromFavorites(Sound sound);
}
