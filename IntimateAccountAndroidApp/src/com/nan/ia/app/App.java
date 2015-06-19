package com.nan.ia.app;

import java.util.HashSet;
import java.util.Set;

import com.nan.ia.app.constant.AppConfigs;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.MainThreadExecutor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

public class App extends Application {
	static private App sApp = null; 
	
	Set<Activity> mCreatedActivities = new HashSet<Activity>();
	ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
		
		@Override
		public void onActivityStopped(Activity activity) {
		}
		
		@Override
		public void onActivityStarted(Activity activity) {
		}
		
		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		}
		
		@Override
		public void onActivityResumed(Activity activity) {
		}
		
		@Override
		public void onActivityPaused(Activity activity) {
		}
		
		@Override
		public void onActivityDestroyed(Activity activity) {
			mCreatedActivities.remove(activity);
		}
		
		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			mCreatedActivities.add(activity);
		}
	};

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
		for (Activity activity : mCreatedActivities) {  
            activity.finish();  
              
        }  

        System.exit(0);
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
