package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.utils.MinDurationWaiter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.content.Intent;

public class LoadingActivity extends BaseActivity {
	private static final long MIN_LOADING_DURATION = 2000;
	
	View mContentView;
    
    @Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContentView = View.inflate(this, R.layout.activity_loading, null);
		setContentView(mContentView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f);
        aa.setDuration(MIN_LOADING_DURATION);
        mContentView.startAnimation(aa);
		
		final MinDurationWaiter waiter = new MinDurationWaiter();
		waiter.begin();
        new AsyncTask<Integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Integer... params) {
				// 初始化程序内容
				BizFacade.getInstance().appInit();
				waiter.waitForDuration(MIN_LOADING_DURATION);
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				loadComplete();
				super.onPostExecute(result);
			}
			
		}.execute(0);
	}
	
	private void loadComplete() {
		if (!AppData.isInit()) {
			// 初始化引导界面
			AppData.setInit(true);
			
			Intent intent = new Intent(LoadingActivity.this, StartActivity.class);
			startActivity(intent);
			finish();
		} else {
			// 主界面
			Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		overridePendingTransition(R.anim.activity_open_enter_alpha, R.anim.activity_open_exit_alpha);
	}
}
