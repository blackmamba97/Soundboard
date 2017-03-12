package com.max.soundboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class RecyclerViewFragment extends Fragment {
    public static final String GROUP_NAME = "groupName";
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_fragment,
                container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = makeAdapter();
        }
        recyclerView.setAdapter(mRecyclerViewAdapter);
        return recyclerView;
    }

    private RecyclerViewAdapter makeAdapter() {
        // Get the group name from the arguments of this fragment.
        SoundManager soundManager = ((SoundActivity) getActivity()).getSoundManager();
        String groupName = getArguments().getString(GROUP_NAME);
        List<Sound> group = soundManager.getSoundGroupByName(groupName);
        boolean isFavoritesFragment = SoundManager.FAVORITES.equals(groupName);
        RecyclerViewAdapter adapter
                = new RecyclerViewAdapter(group, getContext(), isFavoritesFragment);

        // This adapter is needed in the sound manager if a sound is added or deleted from the
        // favorites.
        if (isFavoritesFragment) {
            soundManager.setFavoritesAdapter(adapter);
        }
        return adapter;
    }

    public void notifyItemChanged(Sound sound) {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.notifyItemChanged(sound);
        }
    }

    public void notifyDataSetChanged() {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
