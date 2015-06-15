/**
 * @ClassName:     MinDurationWaiter.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月15日 
 */

package com.nan.ia.app.utils;

public class MinDurationWaiter {
	long beginTime;
	
	public void begin() {
		beginTime = System.currentTimeMillis();
	}
	
	public void waitForDuration(long duration) {
		long restTime = (beginTime + duration) - System.currentTimeMillis();
		if (restTime > 0) {
			// 等待最小时间
			try {
				Thread.sleep(restTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
