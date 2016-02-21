package com.example.millner.barcodescanningapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by Ben on 2/20/2016.
 */
public class PrimaryFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] {"Home", "New", "Search", "Settings"};
    private Context context;

    public PrimaryFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PageFragment.newInstance("Homepage fragment.");
            case 1:
                return AddFragment.newInstance("Add item fragment.");
            case 2:
                return PageFragment.newInstance("Search fragment.");
            case 3:
                return PageFragment.newInstance("Settings fragment.");
            default:
                return PageFragment.newInstance("Homepage fragment.");
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

}
