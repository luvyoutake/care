package com.takecare.takecare.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.takecare.takecare.Activities.BrandedFragment;
import com.takecare.takecare.Activities.GenericFragment;
import com.takecare.takecare.Activities.HerbalFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[] {
      "Generic", "Branded", "Herbal"
    };

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GenericFragment Generic = new GenericFragment();
                return Generic;
            case 1:
                BrandedFragment Branded = new BrandedFragment();
                return Branded;
            case 2:
                HerbalFragment Herbal = new HerbalFragment();
                return Herbal;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
