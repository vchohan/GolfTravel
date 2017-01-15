package com.vchohan.golftravel;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SomeViewPagerAdapter extends FragmentPagerAdapter {

    public SomeViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SomeFragment.newInstance(Color.parseColor("#00BCD4"), position); // cyan
            case 1:
                return SomeFragment.newInstance(Color.parseColor("#F44336"), position); // red
            default:
                return SomeFragment.newInstance(Color.parseColor("#4CAF50"), position); // green
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
