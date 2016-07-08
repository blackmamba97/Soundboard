package com.max.soundboard;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.wnafee.vector.compat.ResourcesCompat;


class FavoriteButtonListener implements View.OnClickListener {
    private final Sound mSound;
    private final RecyclerView mRecyclerView;
    private final boolean mIsFavoritesTab;

    public FavoriteButtonListener(Sound sound, RecyclerView recyclerView, boolean isFavoritesTab) {
        mSound = sound;
        mRecyclerView = recyclerView;
        mIsFavoritesTab = isFavoritesTab;
    }

    @Override
    public void onClick(View v) {
        final ImageButton imageButton = (ImageButton) v;
        final Favorites favorites = SoundManager.getFavorites();
        final String message;

        // Decide if we must add or remove the sound name from the favorites
        if (favorites.contains(mSound)) {
            // Remove sound from the favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star_outline));
            favorites.remove(mSound, mIsFavoritesTab);
            message = String.format(v.getContext().getString(R.string.sound_removed), mSound.getName());
        } else {
            // Add sound to favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star));
            favorites.add(mSound);
            message = String.format(v.getContext().getString(R.string.sound_added), mSound.getName());
        }
        // Notify the user that the action has finished
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }
}
