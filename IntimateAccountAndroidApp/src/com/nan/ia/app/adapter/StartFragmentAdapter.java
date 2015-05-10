package com.nan.ia.app.adapter;

import com.nan.ia.app.R;
import com.nan.ia.app.view.StartFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StartFragmentAdapter extends FragmentPagerAdapter {
	protected static final int[] initBgResid = new int[] { R.drawable.ic_init_step0,
															R.drawable.ic_init_step1,
															R.drawable.ic_init_step2,
															R.drawable.ic_init_step3 };
	
    public StartFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

    @Override
    public Fragment getItem(int position) {
        return StartFragment.newInstance(initBgResid[position % initBgResid.length], position == initBgResid.length - 1);
    }

    @Override
    public int getCount() {
        return initBgResid.length;
    }
}
