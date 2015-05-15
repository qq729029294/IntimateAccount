/**
 * @ClassName:     MainThreadExecutor.java
 * @Description:   帮主主线程执行任务
 * 
 * @author         weijiangnan create on 2015-5-15
 */

package com.nan.ia.app.utils;

import android.os.Handler;
import android.os.Looper;

public class MainThreadExecutor {
	private static Handler sHandler = null;
	
	public static void init() {
		sHandler = new Handler();
	}
	
	/**
	 * {@link #run(Runnable)}
	 * @param runnable
	 */
	public static void run(Runnable runnable) {
		// 没有初始化
		if (null == sHandler) {
			throw new AssertionError("MainThreadExecutor uninit.");
		}
		
		if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
			// 已经在主线程了
			runnable.run();
		} else {
			// 不在主线程，跳转到主线程执行
			sHandler.post(runnable);
		}
	}
}