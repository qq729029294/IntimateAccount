package com.nan.ia.app.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.utils.TimeUtils;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class MainActivity extends BaseActionBarActivity {
    private PinnedHeaderExpandableListView mListViewRecords;
    private RecordsExpandableListAdapter mAdapter;
    private StickyLayout mStickyLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
				MainActivity.this.startActivity(new Intent(MainActivity.this, RecordActivity.class));
			}
		});
		
		enableActionBarGo(getString(R.string.title_login), new Intent(MainActivity.this, AccountBookActivity.class));
		
		initUI();
	}
	
	
	@Override
	protected void onStart() {
		// 刷新数据
		mAdapter.setData(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
				System.currentTimeMillis()));
		mAdapter.notifyDataSetChanged();
		
		super.onStart();
	}

	private void initUI() {
		mListViewRecords = (PinnedHeaderExpandableListView) findViewById(R.id.list_records);
		mAdapter = new RecordsExpandableListAdapter(this);
		mAdapter.setData(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
				System.currentTimeMillis()));
		mListViewRecords.setAdapter(mAdapter);
		mListViewRecords.setDividerHeight(0);
		
        // 展开所有group
        for (int i = 0, count = mListViewRecords.getCount(); i < count; i++) {
        	mListViewRecords.expandGroup(i);
        }

        mListViewRecords.setOnHeaderUpdateListener(mAdapter);
        
//        mListViewRecords.setOnChildClickListener(this);
//        mListViewRecords.setOnGroupClickListener(this);
//        mListViewRecords.setOnGiveUpTouchEventListener(this);
        mStickyLayout = (StickyLayout)findViewById(R.id.sticky_layout);
        mStickyLayout.setOnGiveUpTouchEventListener(new OnGiveUpTouchEventListener() {
			
			@Override
			public boolean giveUpTouchEvent(MotionEvent event) {
		        if (mListViewRecords.getFirstVisiblePosition() == 0) {
		            View view = mListViewRecords.getChildAt(0);
		            if (view != null && view.getTop() >= 0) {
		                return true;
		            }
		        }
		        return false;
			}
		} );
	}
	
	private void initData() {
		
	}
}