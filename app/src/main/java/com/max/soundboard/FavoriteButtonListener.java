package com.max.soundboard;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.wnafee.vector.compat.ResourcesCompat;


class FavoriteButtonListener implements View.OnClickListener {
    private final Sound mSound;
    private final RecyclerViewAdapter mRecyclerViewAdapter;
    private final RecyclerView mRecyclerView;

    public FavoriteButtonListener(Sound sound, RecyclerViewAdapter adapter,
                                  RecyclerView recyclerView) {
        mSound = sound;
        mRecyclerViewAdapter = adapter;
        mRecyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        final ImageButton imageButton = (ImageButton) v;
        final Favorites favorites = SoundManager.getInstance(v.getContext()).getFavorites();
        final String message;

        // Decide if we must add or remove the sound name from the favorites
        if (favorites.contains(mSound)) {
            // Remove sound from the favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star_outline));
            mRecyclerViewAdapter.removeSound(mSound);
            message = String.format(v.getContext().getString(R.string.sound_removed), mSound.getName());
        } else {
            // Add sound to favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star));
            favorites.addSound(mSound);
            message = String.format(v.getContext().getString(R.string.sound_added), mSound.getName());
        }
        // Notify the user that the action has finished
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }
}
