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

    public RecyclerViewAdapter(Group soundGroup, RecyclerView recyclerView) {
        mContext = recyclerView.getContext();
        mSoundGroup = soundGroup;
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.card_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        setupPlayPauseView(viewHolder);
        return viewHolder;
    }

    private void setupPlayPauseView(ViewHolder viewHolder) {
        viewHolder.mPlayPauseView.setStartDrawable(AnimatedVectorDrawable.getDrawable(mContext,
                R.drawable.ic_play_to_pause));
        viewHolder.mPlayPauseView.setEndDrawable(AnimatedVectorDrawable.getDrawable(mContext,
                R.drawable.ic_pause_to_play));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sound sound = mSoundGroup.getSounds().get(position);
        final String soundName = sound.getName();

        // PlayPauseView must match the current state of the sound
        updatePlayPauseView(holder, position);

        // Sound name and group name text view
        updateSoundNameTextView(holder, sound, soundName);

        // Favoring button
        updateFavoriteButton(holder, sound, soundName);

        // The card view recognizes clicks to play the sound
        holder.mCardView.setOnClickListener(new PlayButtonListener(sound));
    }

    private void updatePlayPauseView(ViewHolder holder, int position) {
        if ((holder.mPlayPauseView.getState() == MorphButton.MorphState.END)
                && !mSoundGroup.getSounds().get(position).isPlaying()) {
            holder.mPlayPauseView.setState(MorphButton.MorphState.START, false);
        }
        if ((holder.mPlayPauseView.getState() == MorphButton.MorphState.START)
                && mSoundGroup.getSounds().get(position).isPlaying()) {
            holder.mPlayPauseView.setState(MorphButton.MorphState.END, false);
        }
    }

    private void updateSoundNameTextView(ViewHolder holder, Sound sound, String soundName) {
        holder.mSoundNameTextView.setText(soundName.substring(0, soundName.length() - 4));
        if (mSoundGroup.getName().equals(Favorites.NAME)) {
            // Get the group name of the sounds in the favorites
            String groupName = SoundManager.getInstance(mContext).getGroupNameBySound(sound);
            holder.mGroupNameTextView.setText(groupName);

            // Add bottom padding so the group name has its own space
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.mSoundNameTextView.setPadding(0, 0, 0, (int) (20 * scale + 0.5f));
        } else {
            // Remove bottom padding if group name is not present
            holder.mGroupNameTextView.setVisibility(View.GONE);
            holder.mSoundNameTextView.setPadding(0, 0, 0, 0);
        }
    }

    private void updateFavoriteButton(ViewHolder holder, Sound sound, String soundName) {
        holder.mFavoringButton.setOnClickListener(new FavoriteButtonListener(sound, this,
                mRecyclerView));
        holder.mFavoringButton.setContentDescription(soundName);
        if (SoundManager.getInstance(mContext).getFavorites().contains(sound)) {
            holder.mFavoringButton.setImageDrawable(ResourcesCompat.getDrawable(mContext,
                    R.drawable.ic_star));
        } else {
            holder.mFavoringButton.setImageDrawable(ResourcesCompat.getDrawable(mContext,
                    R.drawable.ic_star_outline));
        }
    }

    @Override
    public int getItemCount() {
        return mSoundGroup.getSounds().size();
    }

    public void removeSound(Sound sound) {
        int index = mSoundGroup.removeSound(sound);
        if (index >= 0)
            notifyItemRemoved(index);
    }

    public void updateSounds() {
        mSoundGroup.updateSounds();
        notifyDataSetChanged();
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
