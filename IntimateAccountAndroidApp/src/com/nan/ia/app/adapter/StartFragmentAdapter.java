package com.nan.ia.app.adapter;

import com.nan.ia.app.R;
import com.nan.ia.app.view.StartFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StartFragmentAdapter extends FragmentPagerAdapter {
	protected static final int[] initBgResid = new int[] { R.drawable.icon_step_0,
															R.drawable.icon_step_1,
															R.drawable.icon_step_2 };
	protected static final String[] texts = new String[] { "想买这个！",
		"还是这个！",
		"或者是这个。。赶快与TA一起记录生活中的点点滴滴吧，规范用度，买想买的!" };
	
    public StartFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

    @Override
    public Fragment getItem(int position) {
        return StartFragment.newInstance(initBgResid[position % initBgResid.length], texts[position % texts.length], position == initBgResid.length - 1);
    }

    @Override
    public int getCount() {
        return initBgResid.length;
    }
}
