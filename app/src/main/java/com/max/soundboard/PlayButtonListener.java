package com.max.soundboard;

import android.view.View;

import com.wnafee.vector.MorphButton;


class PlayButtonListener implements View.OnClickListener {
    private final Sound mSound;

    public PlayButtonListener(Sound sound) {
        mSound = sound;
    }

    @Override
    public void onClick(View v) {
        MorphButton playPauseView = (MorphButton) v.findViewById(R.id.play_pause_view);
        if (mSound.isPlaying()) {
            SoundPlayer.reset();
        } else {
            SoundPlayer.playSound(v.getContext(), mSound, playPauseView);
        }
    }
}
