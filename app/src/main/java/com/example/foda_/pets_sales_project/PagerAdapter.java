package com.example.foda_.pets_sales_project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by foda_ on 2016-07-13.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int count ;
    public PagerAdapter(FragmentManager fm,int count) {
        super(fm);
        this.count=count;
    }

    @Override
    public Fragment getItem(int position) {
     switch (position)
        {
            case 0:
                Advertisement tab1=new Advertisement();
                return  tab1;
            case 1:
                Create_Ad tab2=new Create_Ad();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
