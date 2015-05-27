package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.data.AppData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class LoadingActivity extends BaseActivity {
	private static final long LOADING_DISPLAY_MIN_LENGHT = 2000;
	
	private Handler mHandler = new Handler();
	private long mBeginLoadTime;
    
    @Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
        mBeginLoadTime = System.currentTimeMillis();
        new AsyncTask<Integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Integer... params) {
				// 初始化程序内容
				BizFacade.getInstance().appInit();
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				LoadingActivity.this.beginLoadComplete();
				super.onPostExecute(result);
			}
			
		}.execute(0);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	private void beginLoadComplete() {
		long loadingDisplayTime = (LOADING_DISPLAY_MIN_LENGHT + mBeginLoadTime)
				- System.currentTimeMillis();
		if (loadingDisplayTime > 0) {
			mHandler.postDelayed(new Runnable() {
				public void run() {
					loadComplete();
				}

			}, loadingDisplayTime);
		} else {
			loadComplete();
		}
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
	}
}
