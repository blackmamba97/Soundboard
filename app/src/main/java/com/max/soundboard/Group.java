package com.max.soundboard;

import java.util.ArrayList;


abstract class Group {
    public abstract String getName();
    public abstract ArrayList<Sound> getSounds();
    public abstract void updateSounds();
    public abstract int removeSound(Sound sound);
}
