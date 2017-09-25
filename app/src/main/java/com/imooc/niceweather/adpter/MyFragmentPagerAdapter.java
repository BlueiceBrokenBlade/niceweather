package com.imooc.niceweather.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhx12366 on 2017-09-21.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragList = new ArrayList<Fragment>();

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        this.fragList = fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }
}
