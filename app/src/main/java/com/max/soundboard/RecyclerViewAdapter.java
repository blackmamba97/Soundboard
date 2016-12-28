package com.max.soundboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wnafee.vector.MorphButton;
import com.wnafee.vector.compat.AnimatedVectorDrawable;
import com.wnafee.vector.compat.ResourcesCompat;


class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final Group mSoundGroup;
    private final RecyclerView mRecyclerView;
    private final Favorites mFavorites;
    private final boolean mIsFavoritesAdapter;

    public RecyclerViewAdapter(Group soundGroup, RecyclerView recyclerView,
                               Favorites favorites) {
        mContext = recyclerView.getContext();
        mSoundGroup = soundGroup;
        mRecyclerView = recyclerView;
        mFavorites = favorites;
        mIsFavoritesAdapter = mSoundGroup.getName().equals(Favorites.NAME);
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.card_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        setupPlayPauseView(viewHolder.mPlayPauseView);
        return viewHolder;
    }

    private void setupPlayPauseView(MorphButton mb) {
        mb.setStartDrawable(AnimatedVectorDrawable.getDrawable(mContext,
                R.drawable.ic_play_to_pause));
        mb.setEndDrawable(AnimatedVectorDrawable.getDrawable(mContext,
                R.drawable.ic_pause_to_play));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sound sound = mSoundGroup.getSound(position);

        // Views must match the current state of the sound
        updatePlayPauseView(holder.mPlayPauseView, sound.isPlaying());
        updateTextViews(holder.mSoundNameTextView, holder.mGroupNameTextView, sound);
        updateFavoriteButton(holder.mFavoringButton, sound);

        // The card view recognizes clicks to play the sound
        holder.mCardView.setOnClickListener(new PlayButtonListener(sound));
    }

    private void updatePlayPauseView(MorphButton mb, boolean isPlaying) {
        if (mb.getState() == MorphButton.MorphState.START && isPlaying) {
            mb.setState(MorphButton.MorphState.END, false);
        } else {
            mb.setState(MorphButton.MorphState.START, false);
        }
    }

    private void updateTextViews(TextView soundText, TextView groupText, Sound sound) {
        final String soundName = sound.getName().substring(0, sound.getName().length() - 4);
        soundText.setText(soundName);
        if (mIsFavoritesAdapter) {
            // Get the group name of the sounds in the favorites
            groupText.setText(sound.getGroup().getName());

            // Add bottom padding so the group name has its own space
            final float scale = mContext.getResources().getDisplayMetrics().density;
            soundText.setPadding(0, 0, 0, (int) (20 * scale + 0.5f));
        } else {
            // Remove bottom padding if group name is not present
            groupText.setVisibility(View.GONE);
            soundText.setPadding(0, 0, 0, 0);
        }
    }

    private void updateFavoriteButton(ImageButton ib, Sound sound) {
        ib.setOnClickListener(new FavoriteButtonListener(sound, mRecyclerView, mFavorites,
                mIsFavoritesAdapter));
        ib.setContentDescription(sound.getName());
        if (mFavorites.contains(sound)) {
            ib.setImageDrawable(ResourcesCompat.getDrawable(mContext, R.drawable.ic_star));
        } else {
            ib.setImageDrawable(ResourcesCompat.getDrawable(mContext, R.drawable.ic_star_outline));
        }
    }

    @Override
    public int getItemCount() {
        return mSoundGroup.getSize();
    }

    public void updateItem(Sound sound) {
        notifyItemChanged(mSoundGroup.indexOf(sound));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final MorphButton mPlayPauseView;
        public final TextView mSoundNameTextView;
        public final TextView mGroupNameTextView;
        public final ImageButton mFavoringButton;
        public final CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            mPlayPauseView = (MorphButton) v.findViewById(R.id.play_pause_view);
            mSoundNameTextView = (TextView) v.findViewById(R.id.sound_name_text_view);
            mGroupNameTextView = (TextView) v.findViewById(R.id.group_name_text_view);
            mFavoringButton = (ImageButton) v.findViewById(R.id.favoring_button);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }
}
