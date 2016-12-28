package com.max.soundboard;

import java.util.ArrayList;
import java.util.Iterator;


abstract class Group implements Iterable<Sound> {
    protected final ArrayList<Sound> mSounds = new ArrayList<>();
    protected final String mName;

    protected Group(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public Sound getSound(int position) {
        return mSounds.get(position);
    }

    public int indexOf(Sound sound) {
        for (int i = 0; i < mSounds.size(); i++) {
            if (mSounds.get(i).getName().equals(sound.getName())) {
                return i;
            }
        }
        return -1;
    }

    public int getSize() {
        return mSounds.size();
    }

    public boolean contains(Sound sound) {
        return indexOf(sound) >= 0;
    }

    @Override
    public Iterator<Sound> iterator() {
        return mSounds.iterator();
    }

    @Override
    public String toString() {
        return mName;
    }
}
