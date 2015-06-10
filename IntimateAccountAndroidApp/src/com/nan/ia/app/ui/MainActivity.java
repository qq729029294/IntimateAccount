package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter.ListItemRecord;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.ui.RecordActivity.RecordActivityType;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomPopupMenu;
import com.nan.ia.app.widget.CustomToast;
import com.nan.ia.app.widget.RatioCircleView;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private PinnedHeaderExpandableListView mListViewRecords;
    private RecordsExpandableListAdapter mAdapter;
    private StickyLayout mStickyLayout;
    private RatioCircleView mRatioCircleMain;
    private Button mBtnAccountBookSettings;
    private ResideMenu mResideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		initUI();
	}
	
	@Override
	protected void onStart() {
		refreshData();
		
		mRatioCircleMain.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mRatioCircleMain.beginAnimation(2000);
				
				final TextView textView = (TextView) findViewById(R.id.text_balance);
				
				final TimeAnimator timeAnimator = new TimeAnimator();
				timeAnimator.setTimeListener(new TimeListener() {
					
					@Override
					public void onTimeUpdate(TimeAnimator animation, long totalTime,
							long deltaTime) {
						int text = (int) ((totalTime / 2000.0f) * 500);
						
						if (totalTime >= 2000) {
							text = 500;
							timeAnimator.end();
						}
						
						String str = text + "元";
						SpannableStringBuilder style = new SpannableStringBuilder(str);
//						style.setSpan(new AbsoluteSizeSpan(Utils.sp2px(MainActivity.this, 14)), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//						style.setSpan(new ForegroundColorSpan(MainActivity.this.getResources().getColor(R.color.white_transparent)), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						style.setSpan(new AbsoluteSizeSpan(Utils.sp2px(MainActivity.this, 14)), str.indexOf("元"), str.indexOf("元") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						style.setSpan(new ForegroundColorSpan(MainActivity.this.getResources().getColor(R.color.font_white_ltlt)), str.indexOf("元"), str.indexOf("元") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
						textView.setText(style);
						
						LogUtils.e("totalTime:" + totalTime);
					}
				});
				timeAnimator.start();
			}
		}, 500);
		
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
        
        mRatioCircleMain = (RatioCircleView) findViewById(R.id.ratio_circle_main);
        mRatioCircleMain.addItem(1.0f, Color.rgb(255, 176, 176));
        mRatioCircleMain.addItem(0.7f, Color.rgb(176, 255, 176));
        
        setupTop();
        setupMenu();
        setupListView();
	}
	
	private void setupListView() {
		mListViewRecords = (PinnedHeaderExpandableListView) findViewById(R.id.list_records);
		mListViewRecords.setOnItemLongClickListener(null);
		mAdapter = new RecordsExpandableListAdapter(this, mListViewRecords);
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
				if (null != item) {
					editRecord(item.getAccountRecord());
				}
				
				return false;
			}
		});
        
        mListViewRecords.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long id) {
				final ListItemRecord item = (ListItemRecord) mAdapter.getChild(position);
				if (null == item) {
					return false;
				}
				
				CustomPopupMenu popupMenu = new CustomPopupMenu(MainActivity.this);
				popupMenu.addMenuItem("编辑", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editRecord(item.getAccountRecord());
					}
				});
				
				popupMenu.addMenuItem("删除", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editRecord(item.getAccountRecord());
					}
				});
				
				popupMenu.showAtView(v);
				return true;
			}
		});
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
	
	private void setupTop() {
		findViewById(R.id.btn_settings).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		mBtnAccountBookSettings = (Button) findViewById(R.id.btn_account_book_settings);
		mBtnAccountBookSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 账本设定画面
				startActivity(new Intent(MainActivity.this, AccountBookActivity.class));
			}
		});
		
		mBtnAccountBookSettings.setText(BizFacade.getInstance().getAccountBookById(AppData.getCurrentAccountBookId()).getName());
		
		findViewById(R.id.btn_sync).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<Integer, Integer, Integer>() {

					@Override
					protected Integer doInBackground(Integer... params) {
						BizFacade.getInstance().syncDataToServer();
						return null;
					}
				}.execute(0);
			}
		});
	}
	
	private void refreshData() {
		// 刷新数据
		mAdapter.setData(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
				System.currentTimeMillis()));
		mAdapter.notifyDataSetChanged();
		// 刷新title
		mBtnAccountBookSettings.setText(BizFacade.getInstance().getAccountBookById(AppData.getCurrentAccountBookId()).getName());
	}
	
	private void editRecord(AccountRecord accountRecord) {
		RecordActivity.TransData transData = new RecordActivity.TransData();
		transData.setType(RecordActivityType.EDIT);
		transData.setAccountRecord(accountRecord);
		
		Intent intent = new Intent(MainActivity.this, RecordActivity.class);
		MainActivity.this.startActivity(createTransDataIntent(intent, transData));
	}
}