package com.nan.ia.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter;
import com.nan.ia.app.adapter.RecordsExpandableListAdapter.ListItemRecord;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.dialog.CustomToast;
import com.nan.ia.app.dialog.MaskOperationDialog;
import com.nan.ia.app.entities.AccountBookInfo;
import com.nan.ia.app.entities.AccountBookStatisticalInfo;
import com.nan.ia.app.ui.RecordActivity.RecordActivityType;
import com.nan.ia.app.utils.MinDurationWaiter;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomPopupMenu;
import com.nan.ia.app.widget.RatioCircleView;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.UserInfo;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MainActivity extends BaseActivity {
	private static final long DISPLAY_ANIMATION_DURATION = 1250;
	private static final long DISPLAY_ANIMATION_DELAY = 500;
	private static final long MIN_LOADING_DURATION = 1250;
	
    PinnedHeaderExpandableListView mListViewRecords;
    RecordsExpandableListAdapter mAdapter;
    FrameLayout mLayoutColor;
    StickyLayout mStickyLayout;
    RatioCircleView mRatioCircleMain;
    RelativeLayout mLayoutDetails;
    LinearLayout mLayoutLoading;
    TextView mTextInviteMember;
	TextView mTextBalance;
	TextView mTextIncome;
	TextView mTextExpend;
	TextView mTextLoading;
	TextView mTextNickname;
	
    Button mBtnAccountBookSettings;
    ResideMenu mResideMenu;
    
    BizFacade mBizFacade = BizFacade.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		
		TransData transData = readTransData();
		if (null != transData && transData.isLoginImmediate()) {
			startActivity(new Intent(this, LoginActivity.class));
		}
	}
	
	@Override
	protected void onStart() {
		if (mBizFacade.applyChange(Constant.CHANGE_TYE_USER, this.toString())) {
			// 需要同步数据
			refreshUI();
			doSync();
		} else if (mBizFacade.checkChange(Constant.CHANGE_TYE_CURRENT_ACCOUNT_BOOK, this.toString()) ||
				mBizFacade.checkBookChange(AppData.getCurrentAccountBookId(), this.toString())) {
			// 数据有变动
			refreshUI();
			beginStartAnimation();
		}
		
		super.onStart();
	}

	private void initUI() {
		setupMainContent();
        setupTop();
        setupMenu();
        setupListView();
	}
	
	private void setupMainContent() {
		// 邀请好友
		mTextInviteMember = (TextView) findViewById(R.id.text_invite_member);
		mTextInviteMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mBizFacade.checkLogin(MainActivity.this)) {
					return;
				}
				
				Intent intent = new Intent(MainActivity.this, InviteMemberActivity.class);
				InviteMemberActivity.TransData transData = new InviteMemberActivity.TransData();
				transData.setAccountBookId(AppData.getCurrentAccountBookId());
				startActivity(makeTransDataIntent(intent, transData));
			}
		});
		
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
        
        mLayoutColor = (FrameLayout) findViewById(R.id.layout_color);
        mRatioCircleMain = (RatioCircleView) findViewById(R.id.ratio_circle_main);
        mLayoutDetails = (RelativeLayout) findViewById(R.id.layout_details);
        mLayoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
		mTextBalance = (TextView) findViewById(R.id.text_balance);
		mTextIncome = (TextView) findViewById(R.id.text_income);
		mTextExpend = (TextView) findViewById(R.id.text_expend);
		mTextLoading = (TextView) findViewById(R.id.text_loading);
		
		// 运行加载动画
		ImageView imageView = (ImageView) findViewById(R.id.image_loading);
		((AnimationDrawable) imageView.getBackground()).start();
	}
	
	private void setupListView() {
		mListViewRecords = (PinnedHeaderExpandableListView) findViewById(R.id.list_records);
		mListViewRecords.setOnItemLongClickListener(null);
		mAdapter = new RecordsExpandableListAdapter(this, mListViewRecords);
		mListViewRecords.setAdapter(mAdapter);
		mListViewRecords.setDividerHeight(0);

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
						deleteRecord(item.getAccountRecord().getAccountBookId());
					}
				});
				
				popupMenu.showAtView(v);
				return true;
			}
		});
	}
	
	private void refreshMenu() {
		mResideMenu.removeAllMenuItems();
		
        // create menu items;
		View view = LayoutInflater.from(this).inflate(R.layout.slide_menu_account, null);
		TextView textNickname = (TextView) view.findViewById(R.id.text_nickname);
		if (AppData.getAccountInfo().getAccountType() == Constant.ACCOUNT_TYPE_UNLOGIN) {
			// 未登录，去登录
			textNickname.setText("点击登录");
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, LoginActivity.class));
				}
			});
		} else {
			view.setClickable(false);
			textNickname.setText(AppData.getAccountInfo().getUsername());
		}
		
		mResideMenu.addCustomMenuItem(view, ResideMenu.DIRECTION_LEFT);
		
		ResideMenuItem itemPersonalInfo = new ResideMenuItem(this, R.drawable.icon_personal_info, "个人信息");
		itemPersonalInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO
				CustomToast.showToast("施工中");
			}
		});
		
		mResideMenu.addMenuItem(itemPersonalInfo, ResideMenu.DIRECTION_LEFT);
		
		ResideMenuItem itemAbout = new ResideMenuItem(this, R.drawable.icon_settings, R.string.menu_about);
		itemAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomToast.showToast("客服支持 QQ:729029294");
			}
		});
		
		mResideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
	}
	
	private void setupMenu() {
        // attach to current activity;
		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.drawable.menu_background);
		mResideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
		mResideMenu.setScaleValue(0.6f);
		
		refreshMenu();
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
		
		findViewById(R.id.btn_sync).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doSync();
			}
		});
	}
	
	private void doSync() {
		// 检查登录
		if (!mBizFacade.checkLogin(this)) {
			return;
		}
		
		beginSyncLoadingAnimation();
		MaskOperationDialog.showMask(this);	// 防止操作
		final MinDurationWaiter waiter = new MinDurationWaiter();
		waiter.begin();
		new AsyncTask<Integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Integer... params) {
				// 拉取并处理邀请信息
				if (mBizFacade.pullAndHandleMsgs(MainActivity.this)) {
					// 同步数据
					mBizFacade.syncDataToServer(MainActivity.this);
				}
				
				waiter.waitForDuration(MIN_LOADING_DURATION);
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				MaskOperationDialog.hideMask();
				endLoadingAnimation();
				
				refreshUI();
				beginDisplayAnimation();
				super.onPostExecute(result);
			}
		}.execute(0);
	}
	
	private void refreshUI() {
		// 刷新menu
		if (mBizFacade.applyChange(Constant.CHANGE_TYE_USER, "refreshMenu" + this.toString())) {
			refreshMenu();
		}
		
		AccountBookInfo accountBookInfo = mBizFacade.getAccountBookInfo(AppData
				.getCurrentAccountBookId());
		// 标志已经更新
		mBizFacade.applyBookChange(accountBookInfo.getAccountBook().getAccountBookId(), this.toString());
		mBizFacade.applyChange(Constant.CHANGE_TYE_CURRENT_ACCOUNT_BOOK, this.toString());

		// 详细信息
		mRatioCircleMain.clearItems();
		double income = accountBookInfo.getStatisticalInfo().getIncome();
		double expend = accountBookInfo.getStatisticalInfo().getExpend();
		double denominator = Math.max(Math.abs(income), Math.abs(expend));

//		mTextIncome.setVisibility((income == 0) ? View.GONE : View.VISIBLE);
//		mTextExpend.setVisibility((expend == 0) ? View.GONE : View.VISIBLE);

		if (denominator == 0) {
			// 为空，给一个相等值
			income = 1.0f;
			expend = -1.0f;
			denominator = 1.0f;
		}

		mRatioCircleMain.addItem((float) (income / denominator), getResources()
				.getColor(R.color.white));
		mRatioCircleMain.addItem((float) (-expend / denominator),
				getResources().getColor(R.color.white));

		// 刷新记录ListView数据
		mAdapter.setData(mBizFacade.getMoreAccountRecords(
				AppData.getCurrentAccountBookId(), System.currentTimeMillis()));
		mAdapter.notifyDataSetChanged();

		// 展开所有group
		mListViewRecords.post(new Runnable() {

			@Override
			public void run() {
				for (int i = 0, count = mAdapter.getGroupCount(); i < count; i++) {
					mListViewRecords.expandGroup(i);
				}
			}
		});
		
		// 刷新title
		mBtnAccountBookSettings.setText(mBizFacade.getAccountBookById(
				AppData.getCurrentAccountBookId()).getName());
		
		// 邀请好友
		List<UserInfo> memberUserInfos = accountBookInfo.getMemberUserInfos();
		if (memberUserInfos.size() > 1) {
			// 有其他成员
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < memberUserInfos.size(); i++) {
				if (sb.length() > 0) {
					sb.append("、");
				}
				
				if (memberUserInfos.get(i).getUserId() == AppData.getAccountInfo().getUserId()) {
					sb.append("我");
				} else {
					sb.append(memberUserInfos.get(i).getNickname());
				}
			}
			
			sb.append(String.format("共位%d成员，添加＋", memberUserInfos.size()));
			mTextInviteMember.setText(sb.toString());
		} else {
			mTextInviteMember.setText("邀请小伙伴，一起来记录账本吧＋");
		}
	}
	
	private void beginStartAnimation() {
		// 开始加载动画
		mRatioCircleMain.postDelayed(new Runnable() {

			@Override
			public void run() {
				beginLoadingAnimation();

				mRatioCircleMain.postDelayed(new Runnable() {

					@Override
					public void run() {
						endLoadingAnimation();
						
						// 开始展示动画
						beginDisplayAnimation();
					}
				}, MIN_LOADING_DURATION);
			}
		}, DISPLAY_ANIMATION_DELAY);
	}
	
	private void beginLoadingAnimation() {
		mRatioCircleMain.startLoadingAnimation();
		mLayoutDetails.setVisibility(View.GONE);
		mLayoutLoading.setVisibility(View.VISIBLE);
		
		mTextLoading.setText("拼命加载中~");
		
		beginColorAnimation(((ColorDrawable) mLayoutColor.getBackground()).getColor(),
				getResources().getColor(R.color.main_color_dkdk), MIN_LOADING_DURATION);
	}
	
	private void beginSyncLoadingAnimation() {
		beginLoadingAnimation();
		
		mTextLoading.setText("同步数据中~");
	}
	
	private void endLoadingAnimation() {
		mRatioCircleMain.stopLoadingAnimation();
		mLayoutDetails.setVisibility(View.VISIBLE);
		mLayoutLoading.setVisibility(View.GONE);
	}
	
	private void beginColorAnimation(int baseColor, int newColor, long duration) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(mLayoutColor, "backgroundColor", baseColor, newColor);
        colorAnim.setDuration(DISPLAY_ANIMATION_DURATION);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }
	
	private void beginDisplayAnimation() {
		final AccountBookStatisticalInfo statisticalInfo = mBizFacade
				.getAccountBookInfo(AppData.getCurrentAccountBookId())
				.getStatisticalInfo();
		final double balance = statisticalInfo.getBalance();
		final double income = statisticalInfo.getIncome();
		final double expend = statisticalInfo.getExpend();

		// 背景色动画
		int baseColor = getResources().getColor(R.color.main_color_dkdk);
		int targetColor = baseColor;
		double colorRatio = 0.0f;
		if (Math.abs(income) > Math.abs(expend)) {
			if (expend == 0) {
				colorRatio = 1.0f;
			} else {
				colorRatio = Math.abs(income) / Math.abs(expend) - 1.0f;
			}
			
			targetColor = getResources().getColor(R.color.income_dk);

		} else if (Math.abs(income) < Math.abs(expend)) {
			if (income == 0) {
				colorRatio = 1.0f;
			} else {
				colorRatio = Math.abs(expend) / Math.abs(income) - 1.0f;
			}
			
			targetColor = getResources().getColor(R.color.expend_dk);
		}
		
		colorRatio = Math.min(1.0f, colorRatio);
		int newColor = Utils.getRatioColor(baseColor, targetColor, (float) colorRatio);
		beginColorAnimation(baseColor, newColor, DISPLAY_ANIMATION_DURATION);
        
        // 旋转动画
		mRatioCircleMain.beginAnimation(DISPLAY_ANIMATION_DURATION);

		// 数值动画
		final TimeAnimator timeAnimator = new TimeAnimator();
		timeAnimator.setTimeListener(new TimeListener() {

			@Override
			public void onTimeUpdate(TimeAnimator animation, long totalTime,
					long deltaTime) {
				float ratio = (totalTime / (float) DISPLAY_ANIMATION_DURATION);
				double curBalance = (double) (int) (ratio * balance);
				double curIncome = (double) (int) (ratio * income);
				double curExpend = (double) (int) (ratio * expend);

				if (totalTime >= DISPLAY_ANIMATION_DURATION) {
					curBalance = balance;
					curIncome = income;
					curExpend = expend;
					timeAnimator.end();
				}

				// 结余
				String str = Utils.formatCNY(curBalance);
				SpannableStringBuilder style = new SpannableStringBuilder(str);
//				style.setSpan(
//						new AbsoluteSizeSpan(Utils.sp2px(MainActivity.this, 14)),
//						str.indexOf("元"), str.indexOf("元") + 1,
//						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//				style.setSpan(new ForegroundColorSpan(MainActivity.this
//						.getResources().getColor(R.color.font_white_ltlt)), str
//						.indexOf("元"), str.indexOf("元") + 1,
//						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				mTextBalance.setText(style);

				// 收入/支出
				str = "+" + Utils.formatCNY(curIncome) + "/" + Utils.formatCNY(curExpend);
				mTextIncome.setText(str);

				// 支出
//				str = Utils.formatCNY(curExpend);
//				mTextExpend.setText(str);
			}
		});

		timeAnimator.start();
	}
	
	private void editRecord(AccountRecord record) {
		RecordActivity.TransData transData = new RecordActivity.TransData();
		transData.setType(RecordActivityType.EDIT);
		transData.setAccountRecord(record);
		
		Intent intent = new Intent(MainActivity.this, RecordActivity.class);
		MainActivity.this.startActivity(makeTransDataIntent(intent, transData));
	}
	
	private void deleteRecord(int accountRecordId) {
		mBizFacade.deleteAccountRecord(accountRecordId);
	}
	
	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		boolean loginImmediate;
		public boolean isLoginImmediate() {
			return loginImmediate;
		}

		public void setLoginImmediate(boolean loginImmediate) {
			this.loginImmediate = loginImmediate;
		}
	}
}