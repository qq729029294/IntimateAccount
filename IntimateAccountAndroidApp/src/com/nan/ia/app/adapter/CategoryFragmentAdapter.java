package com.nan.ia.app.adapter;

import java.util.List;

import com.nan.ia.app.data.AppData;
import com.nan.ia.app.view.CategoryFragment;
import com.nan.ia.common.entities.AccountCategory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryFragmentAdapter extends FragmentPagerAdapter {
	static List<AccountCategory> mAccountCategories;
	
    public CategoryFragmentAdapter(FragmentManager fm, List<AccountCategory> accountCategories) {
		super(fm);
		mAccountCategories = accountCategories;
	}

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(position, mAccountCategories);
    }

    @Override
    public int getCount() {
    	int count = mAccountCategories.size() / CategoryGridAdapter.PER_PAGER_ITEM_COUNT;
    	count += (mAccountCategories.size() == 0 ||
    			((mAccountCategories.size() % CategoryGridAdapter.PER_PAGER_ITEM_COUNT) != 0))
    			? 1: 0;
        return count;
    }
}
