package com.nan.ia.app.ui;


import com.nan.ia.app.R;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter.ListItemRecord;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.ui.EditAccountBookActivity.EditAccountBookType;
import com.nan.ia.app.ui.RecordActivity.RecordActivityType;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomActionBar;
import com.nan.ia.app.widget.CustomToast;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActionBarActivity {
    private PinnedHeaderExpandableListView mListViewRecords;
    private RecordsExpandableListAdapter mAdapter;
    private StickyLayout mStickyLayout;
    
    private ResideMenu mResideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
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
		// 记一笔
		findViewById(R.id.btn_record).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 新建记录，记一笔画面
				MainActivity.this.startActivity(new Intent(MainActivity.this, RecordActivity.class));
			}
		});
		
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
        mListViewRecords.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				ListItemRecord item = (ListItemRecord) mAdapter.getChild(groupPosition, childPosition);
				RecordActivity.TransData transData = new RecordActivity.TransData();
				transData.setType(RecordActivityType.EDIT);
				transData.setAccountRecord(item.getAccountRecord());
				
				Intent intent = new Intent(MainActivity.this, RecordActivity.class);
				MainActivity.this.startActivity(createTransDataIntent(intent, transData));
				return false;
			}
		});
        
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
        
        setupMenu();
        
        setupActionBar();
	}
	
	private void setupMenu() {
        // attach to current activity;
		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.drawable.menu_background);
		mResideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
		mResideMenu.setScaleValue(0.6f);

        // create menu items;
		
		View view = LayoutInflater.from(this).inflate(R.layout.slide_menu_account, null);
		mResideMenu.addCustomMenuItem(view, ResideMenu.DIRECTION_LEFT);
		
		ResideMenuItem itemLogin = new ResideMenuItem(this, R.drawable.icon_login, R.string.menu_account_login);
		itemLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}
		});
		mResideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_LEFT);
		
		ResideMenuItem itemAbout = new ResideMenuItem(this, R.drawable.icon_settings, R.string.menu_about);
		itemAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomToast.showToast("客服支持 QQ:729029294");
			}
		});
		
        mResideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
	}
	
	private void setupActionBar() {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.selector_btn_setting);
		imageView.setClickable(true);
		imageView.setLayoutParams(new LayoutParams(Utils.dip2px(this, 32), Utils.dip2px(this, 32)));
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		mActionBar.customLeftView(this, imageView);
		
		View mainTitle = LayoutInflater.from(this).inflate(R.layout.view_main_title, null);
		mainTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 账本设定画面
				startActivity(new Intent(MainActivity.this, AccountBookActivity.class));
			}
		});
		TextView textTitle = (TextView) mainTitle.findViewById(R.id.text_account_book_name);
		textTitle.setText(BizFacade.getInstance().getAccountBookById(AppData.getCurrentAccountBookId()).getName());
		mActionBar.customCenterView(this, mainTitle);
	}
}