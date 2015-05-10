package com.nan.ia.app;

import com.nan.common.app.utils.LogUtil;

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
		
		LogUtil.configure(AppConfigs.TAG, LogUtil.VERBOSE);
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
	    @SuppressWarnings("deprecation")
		ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
	    String currentPackageName = componentName.getPackageName();
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
	    {
	        return true ;
	    }
	 
	    return false ;
	}
}
