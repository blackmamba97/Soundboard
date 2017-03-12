package com.max.soundboard;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wnafee.vector.MorphButton;
import com.wnafee.vector.compat.AnimatedVectorDrawable;

import java.util.List;


class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Sound> mSoundGroup;
    private final SoundManager mSoundManager;
    private final boolean mIsFavoritesAdapter;
    private final int mPadding;

    public RecyclerViewAdapter(List<Sound> soundGroup, Context context,
                               boolean isFavoritesAdapter) {
        mContext = context;
        mSoundGroup = soundGroup;
        mSoundManager = ((SoundActivity) mContext).getSoundManager();
        mIsFavoritesAdapter = isFavoritesAdapter;
        mPadding = (int) (20 * mContext.getResources().getDisplayMetrics().density + 0.5f);
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
        Sound sound = mSoundGroup.get(position);

        // Views must match the current state of the sound.
        updatePlayPauseView(holder.mPlayPauseView, sound.isPlaying());
        updateTextViews(holder.mSoundNameTextView, holder.mGroupNameTextView, sound);
        updateFavoriteButton(holder.mFavoringButton, sound);

        // The card view recognizes clicks to play the sound.
        holder.mCardView.setOnClickListener(v -> {
            if (sound.getDirectory().exists()) {
                if (sound.isPlaying()) {
                    SoundPlayer.reset(mContext);
                } else {
                    SoundPlayer.playSound(mContext, sound, holder.mPlayPauseView);
                }
            } else {
                String message = String.format(mContext.getString(R.string.sound_not_found),
                        sound.getName());
                Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void updatePlayPauseView(MorphButton mb, boolean isPlaying) {
        if (mb.getState() == MorphButton.MorphState.START && isPlaying) {
            mb.setState(MorphButton.MorphState.END, false);
        } else {
            mb.setState(MorphButton.MorphState.START, false);
        }
    }

    private void updateTextViews(TextView soundText, TextView groupText, Sound sound) {
        // Remove the file ending from the displayed name.
        String soundName = sound.getName().substring(0, sound.getName().length() - 4);
        soundText.setText(soundName);

        if (mIsFavoritesAdapter) {
            // Get the group name of the sounds in the favorites.
            groupText.setText(sound.getGroupName());

            // Add bottom padding so the group name has its own space.
            soundText.setPadding(0, 0, 0, mPadding);
        } else {
            // Remove bottom padding if group name is not present.
            groupText.setVisibility(View.GONE);
            soundText.setPadding(0, 0, 0, 0);
        }
    }

    private void updateFavoriteButton(ImageButton ib, Sound sound) {
        ib.setOnClickListener(v -> {
            int messageId;
            int imageId;

            // Decide if we must add or remove the sound name from the favorites.
            if (mSoundManager.isFavored(sound)) {
                messageId = R.string.sound_removed;
                imageId = R.drawable.ic_star_outline;
                mSoundManager.removeFromFavorites(sound);

                if (mIsFavoritesAdapter) {
                    // Update the sound in the original tab.
                    SoundActivity activity = (SoundActivity) mContext;
                    RecyclerViewFragment fragment = (RecyclerViewFragment)
                            activity.getViewPagerAdapter().getFragment(sound.getGroupName());
                    fragment.notifyItemChanged(sound);
                }
            } else {
                messageId = R.string.sound_added;
                imageId = R.drawable.ic_star;
                mSoundManager.addToFavorites(sound);
            }
            // Update the star icon.
            ib.setImageResource(imageId);

            // Notify the user that the action has finished.
            String message = String.format(mContext.getString(messageId), sound.getName());
            Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
        });
        ib.setContentDescription(sound.getName());

        if (mSoundManager.isFavored(sound)) {
            ib.setImageResource(R.drawable.ic_star);
        } else {
            ib.setImageResource(R.drawable.ic_star_outline);
        }
    }

    @Override
    public int getItemCount() {
        return mSoundGroup.size();
    }

    public void notifyItemChanged(Sound sound) {
        notifyItemChanged(mSoundGroup.indexOf(sound));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
