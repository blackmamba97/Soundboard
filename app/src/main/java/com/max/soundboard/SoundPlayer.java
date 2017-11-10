package com.max.soundboard;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.wnafee.vector.MorphButton;


class SoundPlayer {
    private static MediaPlayer mMediaPlayer;
    private static MorphButton mMorphButton;
    private static Sound mLastPlayedSound;

    private SoundPlayer() {}

    public static void playSound(Context context, Sound sound, MorphButton playPauseView) {
        reset(context);
        // Remember the sound to reset it if another sound is played.
        mLastPlayedSound = sound;
        mLastPlayedSound.setPlaying(true);

        // The old play pause view has been reset and the new one gets active now.
        mMorphButton = playPauseView;
        mMorphButton.setState(MorphButton.MorphState.END, true);

        // Start the media player.
        mMediaPlayer = MediaPlayer.create(context, Uri.fromFile(sound.getDirectory()));
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(mp -> reset(context));
        mMediaPlayer.start();
    }

    public static void reset(Context context) {
        if (mMorphButton != null) {
            mMorphButton.setState(MorphButton.MorphState.START, true);
            mMorphButton = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mLastPlayedSound != null) {
            mLastPlayedSound.setPlaying(false);

            // If a playing sound is favored it needs to be updated in the favorites tab.
            ((SoundActivity) context).getSoundManager().notifySoundChanged(mLastPlayedSound);

            mLastPlayedSound = null;
        }
    }
}
