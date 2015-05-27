/**
 * @ClassName:     BaseActionBarActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月19日 
 */

package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.widget.CustomActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

public abstract class BaseActionBarActivity extends BaseActivity {
	protected static final String EXTRA_KEY_FROM_TITLE = "EXTRA_KEY_FROM_TITLE";
	protected static final String EXTRA_KEY_TO_TITLE = "EXTRA_KEY_TO_TITLE";
	
	CustomActionBar mActionBar = null;
	RelativeLayout mLayoutContainer = null;
	View mContentView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口无标题栏
		
		super.setContentView(R.layout.activity_actionbar_base);
		mActionBar = (CustomActionBar) getWindow().getDecorView().findViewById(R.id.actionBar);
		mLayoutContainer = (RelativeLayout) getWindow().getDecorView().findViewById(R.id.layout_container);
		
		initActionBar();
	}
	
	// 初始化动作栏
	protected void initActionBar() {
		// 返回按钮
		String fromTitle = getIntent().getStringExtra(EXTRA_KEY_FROM_TITLE);
		if (null != fromTitle && !fromTitle.isEmpty()) {
			mActionBar.enableBack(fromTitle, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BaseActionBarActivity.this.finish();
				}
			});
		}
		
		// Title
		String toTitle = getIntent().getStringExtra(EXTRA_KEY_TO_TITLE);
		if (null == toTitle || toTitle.isEmpty()) {
			toTitle = getTitle().toString();
		}
		
		mActionBar.setTitle(toTitle);
	}
	
	protected void enableActionBarGo(String text, final Intent startActivityIntent) {
		mActionBar.enableGo(text, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != startActivityIntent) {
					BaseActionBarActivity.this.startActivity(startActivityIntent);
				}
			}
		});
	}

	protected View getContentView() {
		return mContentView;
	}

	// 重写ContentView部分，使正常Activity不需要做过多修改
	@Override
	public void setContentView(int layoutResID) {
		mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
		mLayoutContainer.removeAllViews();
		mLayoutContainer.addView(mContentView);
	}

	@Override
	public void setContentView(View view) {
		mContentView = view;
		mLayoutContainer.removeAllViews();
		mLayoutContainer.addView(mContentView);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		mContentView = view;
		mLayoutContainer.removeAllViews();
		mLayoutContainer.addView(mContentView, params);
	}
	
	@Override
	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	// 重新startActivity/finish部分，加入动画，传递导航栏值
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra(EXTRA_KEY_FROM_TITLE, getFromTitle());
		super.startActivityForResult(intent, requestCode);
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode,
			Bundle options) {
		intent.putExtra(EXTRA_KEY_FROM_TITLE, getFromTitle());
		super.startActivityForResult(intent, requestCode, options);
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void startActivity(Intent intent) {
		intent.putExtra(EXTRA_KEY_FROM_TITLE, getFromTitle());
		super.startActivity(intent);
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		intent.putExtra(EXTRA_KEY_FROM_TITLE, getFromTitle());
		super.startActivity(intent, options);
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void startActivityWithToTitle(Intent intent, String toTitle) {
		intent.putExtra(EXTRA_KEY_FROM_TITLE, getFromTitle());
		intent.putExtra(EXTRA_KEY_TO_TITLE, toTitle);
		super.startActivity(intent);
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		
		if (mActionBar.canBack()) {
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mActionBar.setTitle(title.toString());
		super.setTitle(title);
	}

	@Override
	public void setTitle(int titleId) {
		mActionBar.setTitle(getString(titleId));
		super.setTitle(titleId);
	}

	/**
	 * 获得跳转的Title
	 */
	protected String getFromTitle() {
		String title = getTitle().toString();
		if (null == title || title.isEmpty()) {
			title = getString(R.string.title_default_back);
		}
		
		return title;
	}
}
