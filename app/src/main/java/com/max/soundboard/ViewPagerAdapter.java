package com.max.soundboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final FragmentManager mFragmentManager;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentManager = fragmentManager;
    }

    @Override @NonNull
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public Fragment getFragment(String title) {
        return mFragmentList.get(mFragmentTitleList.indexOf(title));
    }

    public void removeFragment(int position) {
        // Fragment must be removed from the manager to get the correct order of fragments while
        // switching tabs.
        List<Fragment> fragments = mFragmentManager.getFragments();
        fragments.remove(mFragmentList.get(position));

        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }
}
