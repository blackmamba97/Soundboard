package com.max.soundboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;


public class RecyclerViewFragment extends Fragment {
    public static final String GROUP_NAME = "groupName";
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view;

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Set the layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        // Add an item animator
        recyclerView.setItemAnimator(new FadeInAnimator());

        // Get the group name from the arguments of this fragment
        String groupName = getArguments().getString(GROUP_NAME);

        // Get the group from the SoundManager
        Group group;
        if (groupName != null && groupName.equals(Favorites.NAME)) {
             group = SoundManager.getInstance(getContext()).getFavorites();
        } else {
            group = SoundManager.getInstance(getContext()).getSoundGroupByName(groupName);
        }
        // Create a adapter which holds the group
        mRecyclerViewAdapter = new RecyclerViewAdapter(group, recyclerView);
        mRecyclerViewAdapter.updateSounds();
        recyclerView.setAdapter(mRecyclerViewAdapter);

        return view;
    }

    public RecyclerViewAdapter getAdapter() {
        return mRecyclerViewAdapter;
    }
}
