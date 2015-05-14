package com.nan.ia.app.utils;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	static public byte[] bitmapToByteArray(Bitmap bm) {
		int size = bm.getWidth() * bm.getHeight() * 4;  
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        byte[] imagedata = baos.toByteArray();
        
        return imagedata;
	}
	
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  

    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }
    
	static public void KeyBoardCancle(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	public static String hexString(byte[] source) {
		if (source == null || source.length <= 0) {
			return "";
		}

		final int size = source.length;
		final char str[] = new char[size * 2];
		int index = 0;
		byte b;
		for (int i = 0; i < size; i++) {
			b = source[i];
			str[index++] = sHexDigits[b >>> 4 & 0xf];
			str[index++] = sHexDigits[b & 0xf];
		}
		return new String(str);
	}
}