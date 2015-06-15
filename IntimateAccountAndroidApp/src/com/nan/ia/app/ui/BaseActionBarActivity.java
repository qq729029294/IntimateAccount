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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

public abstract class BaseActionBarActivity extends BaseActivity {
	protected static final String EXTRA_KEY_TO_TITLE = "EXTRA_KEY_TO_TITLE";

	CustomActionBar mActionBar = null;
	RelativeLayout mLayoutContainer = null;
	View mContentView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置窗口无标题栏

		super.setContentView(R.layout.activity_actionbar_base);
		mActionBar = (CustomActionBar) getWindow().getDecorView().findViewById(
				R.id.actionBar);
		mLayoutContainer = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.layout_container);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		initActionBar();
	}

	// 初始化动作栏
	protected void initActionBar() {
		// 返回按钮
		mActionBar.enableBack("", new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActionBarActivity.this.finish();
			}
		});

		// Title
		String toTitle = getIntent().getStringExtra(EXTRA_KEY_TO_TITLE);
		if (null == toTitle || toTitle.isEmpty()) {
			toTitle = getTitle().toString();
		}

		mActionBar.setTitle(toTitle);
	}

	protected void enableActionBarGo(String text,
			final Runnable runnable) {
		mActionBar.enableGo(text, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != runnable) {
					runnable.run();
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

	public void startActivityWithToTitle(Intent intent, String toTitle) {
		intent.putExtra(EXTRA_KEY_TO_TITLE, toTitle);
		super.startActivity(intent);
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
}
