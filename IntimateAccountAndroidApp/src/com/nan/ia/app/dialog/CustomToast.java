/**
 * @ClassName:     CustomToast.java
 * @Description:   TODO 
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app.dialog;

import java.util.ArrayList;
import java.util.List;

import com.nan.ia.app.App;
import com.nan.ia.app.utils.MinDurationWaiter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class CustomToast extends Toast {
	public CustomToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	static final long MIN_TOAST_SHOW_DURATION = 300; 
	static Toast sLastToast;
	static List<ToastInfo> sToastInfos = new ArrayList<ToastInfo>();
	static MinDurationWaiter sMinShowWaiter = new MinDurationWaiter();

	public static void showToast(String text) {
		if (sMinShowWaiter.isWaitTimeout(MIN_TOAST_SHOW_DURATION)) {
			synchronized (sToastInfos) {
				ToastInfo info = new ToastInfo();
				info.text = text;
				sToastInfos.add(info);
			}
		}
		
		sMinShowWaiter.begin();
		sLastToast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
		sLastToast.show();
		
		synchronized (sToastInfos) {
			if (sToastInfos.size() > 0) {
				new AsyncTask<Integer, Integer, Integer>() {

					@Override
					protected Integer doInBackground(Integer... params) {
						sMinShowWaiter.waitForDuration(MIN_TOAST_SHOW_DURATION);
						return null;
					}

					@Override
					protected void onPostExecute(Integer result) {
						if (null != sLastToast) {
							sLastToast.cancel();
						}

						synchronized (sToastInfos) {
							CustomToast.showToast(sToastInfos.remove(0).text);
						}
						
						super.onPostExecute(result);
					}
				};
			}
		}
	}
	
	public static void showToast(int resId) {
		showToast(App.getInstance().getString(resId));
	}
	
	private static class ToastInfo {
		String text;
	}
}