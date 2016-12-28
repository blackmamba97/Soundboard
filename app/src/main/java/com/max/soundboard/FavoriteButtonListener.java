package com.max.soundboard;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.wnafee.vector.compat.ResourcesCompat;


class FavoriteButtonListener implements View.OnClickListener {
    private final Sound mSound;
    private final RecyclerView mRecyclerView;
    private final Favorites mFavorites;
    private final boolean mIsFavoritesTab;

    public FavoriteButtonListener(Sound sound, RecyclerView recyclerView, Favorites favorites,
                                  boolean isFavoritesTab) {
        mSound = sound;
        mRecyclerView = recyclerView;
        mFavorites = favorites;
        mIsFavoritesTab = isFavoritesTab;
    }

    @Override
    public void onClick(View v) {
        final ImageButton imageButton = (ImageButton) v;
        final String message;

        // Decide if we must add or remove the sound name from the favorites
        if (mFavorites.contains(mSound)) {
            // Remove sound from the favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star_outline));
            mFavorites.remove(mSound);
            message = String.format(v.getContext().getString(R.string.sound_removed), mSound.getName());
            if (mIsFavoritesTab) {
                updateItemInOriginTab();
            }
        } else {
            // Add sound to favorites
            imageButton.setImageDrawable(ResourcesCompat.getDrawable(v.getContext(),
                    R.drawable.ic_star));
            mFavorites.add(mSound);
            message = String.format(v.getContext().getString(R.string.sound_added), mSound.getName());
        }
        // Notify the user that the action has finished
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    private void updateItemInOriginTab() {
        String groupName = mSound.getGroup().getName();
        ViewPagerAdapter viewPagerAdapter
                = ((SoundActivity) mRecyclerView.getContext()).getViewPagerAdapter();
        RecyclerViewAdapter origAdapter
                = ((RecyclerViewFragment) viewPagerAdapter.getFragment(groupName)).getAdapter();
        if (origAdapter != null) {
            origAdapter.updateItem(mSound);
        }
    }
}
