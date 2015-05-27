package com.nan.ia.app.view;

import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.CategoryGridAdapter;
import com.nan.ia.common.entities.AccountCategory;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class CategoryFragment extends Fragment {
    private List<AccountCategory> mAccountCategories;
    
    public static CategoryFragment newInstance(int position, List<AccountCategory> accountCategories) {
    	CategoryFragment fragment = new CategoryFragment();
        fragment.mPosition = position;
        fragment.mAccountCategories = accountCategories;
        return fragment;
    }

    private int mPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    	GridView gridView = new GridView(getActivity());
//    	gridView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//    			android.view.ViewGroup.LayoutParams.MATCH_PARENT));
//    	gridView.setAdapter(new CategoryGridAdapter(getActivity(), mPosition));
//    	gridView.setColumnWidth(Utils.dip2px(getActivity(), 100));
    	
    	GridView gridView = (GridView) LayoutInflater.from(getActivity()).inflate(R.layout.grid_category, null);
		gridView.setAdapter(new CategoryGridAdapter(getActivity(), mPosition, mAccountCategories));
        return gridView;
    }
}