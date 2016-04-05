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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        setupRecyclerView((RecyclerView) view);
        return view;
    }

    private void setupRecyclerView(RecyclerView view) {
        view.setHasFixedSize(true);
        view.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        view.setItemAnimator(new FadeInAnimator());
        // Create a adapter which holds the group
        mRecyclerViewAdapter = new RecyclerViewAdapter(getGroup(), view);
        view.setAdapter(mRecyclerViewAdapter);
    }

    private Group getGroup() {
        // Get the group name from the arguments of this fragment
        String groupName = getArguments().getString(GROUP_NAME);

        // Get the group from the SoundManager
        if (groupName != null && groupName.equals(Favorites.NAME)) {
            return SoundManager.getFavorites();
        } else {
            return SoundManager.getSoundGroupByName(groupName);
        }
    }

    public RecyclerViewAdapter getAdapter() {
        return mRecyclerViewAdapter;
    }
}
