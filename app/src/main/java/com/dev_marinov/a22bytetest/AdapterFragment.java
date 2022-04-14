package com.dev_marinov.a22bytetest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterFragment extends FragmentPagerAdapter {

    int tabCount;

    public AdapterFragment(@NonNull FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new FragmentHome();
            case 1: return new FragmentSport();
            case 2: return new FragmentBusiness();
            case 3: return new FragmentEntertainment();
            case 4: return new FragmentHealth();
            case 5: return new FragmentScience();
            case 6: return new FragmentTechnology();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
