package com.max.soundboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RecyclerViewFragment extends Fragment {
    public static final String GROUP_NAME = "groupName";
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_fragment,
                container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        // Get the group name from the arguments of this fragment.
        SoundManager soundManager = ((SoundActivity) getActivity()).getSoundManager();
        Favorites favorites = soundManager.getFavorites();
        String groupName = getArguments().getString(GROUP_NAME);
        Group group = soundManager.getSoundGroupByName(groupName);

        mRecyclerViewAdapter = new RecyclerViewAdapter(group, recyclerView, favorites);
        recyclerView.setAdapter(mRecyclerViewAdapter);
        favorites.setFavoritesAdapter(mRecyclerViewAdapter);
        return recyclerView;
    }

    public RecyclerViewAdapter getAdapter() {
        return mRecyclerViewAdapter;
    }
}
