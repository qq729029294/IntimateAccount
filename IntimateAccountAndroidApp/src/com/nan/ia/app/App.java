package com.nan.ia.app;

import com.nan.ia.app.constant.AppConfigs;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.MainThreadExecutor;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

public class App extends Application {
	static private App sApp = null; 
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		sApp = this;
		
		LogUtils.configure(AppConfigs.TAG, LogUtils.VERBOSE);
		MainThreadExecutor.init();
		
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	static public App getInstance() {
		return sApp; 
	}
	
	/**
	 * 退出应用
	 */
	public void exitApp() {
		
	}
	
	/**
	 * 判断是否在背景运行
	 * @return
	 */
	public boolean isRunningForeground ()
	{
	    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
	    String currentPackageName = componentName.getPackageName();
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
	    {
	        return true ;
	    }
	 
	    return false ;
	}
}
