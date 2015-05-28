/**
 * @ClassName:     RecordActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Scroller;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.CategoryGridAdapter;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.widget.CustomSwipeListView;

public class RecordActivity extends BaseActionBarActivity {
	CustomSwipeListView mListView = null;
	Scroller mScroller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_record);
		
    	GridView gridView = (GridView) findViewById(R.id.grid_category);
		gridView.setAdapter(new CategoryGridAdapter(this, AppData.getAccountCategories()));
		
//        ViewPager pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(new CategoryFragmentAdapter(getSupportFragmentManager(), AppData.getAccountCategories()));
//
//        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(pager);
//        indicator.setRadius(Utils.dip2px(this, 4));
//        indicator.setPageColor(Color.rgb(240, 240, 240));
//        indicator.setFillColor(getResources().getColor(R.color.app_main_color));
//        indicator.setStrokeColor(Color.rgb(196, 196, 196));
	}
}