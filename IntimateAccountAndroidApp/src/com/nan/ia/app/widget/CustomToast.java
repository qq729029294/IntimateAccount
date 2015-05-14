/**
 * @ClassName:     CustomToast.java
 * @Description:   TODO 
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.App;

import android.content.Context;
import android.widget.Toast;

public class CustomToast extends Toast {
	public CustomToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static void showToast(String text) {
		Toast.makeText(App.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToast(int resId) {
		Toast.makeText(App.getInstance().getApplicationContext(), App.getInstance().getApplicationContext().getText(resId), Toast.LENGTH_SHORT).show();
	}
}