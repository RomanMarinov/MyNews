package com.dev_marinov.a22bytetest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPager2Adapter extends FragmentStateAdapter {

    // переменная содержит фрагменты, к которым ViewPager2 позволяет нам переходить.
    private ArrayList<Fragment> fragments;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //Log.e("444","ViewPager2Adapter createFragment position=" + position);
        //Log.e("444","ViewPager2Adapter createFragment fragments.get(position)=" + fragments.get(position));
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        //Log.e("444","ViewPager2Adapter fragments.size()" + fragments.size());
        return fragments.size();
    }

    public void setData(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
        //Log.e("444","ViewPager2Adapter вызвался setData fragments" + fragments.size());
    }

}