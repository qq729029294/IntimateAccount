package com.nan.ia.app.utils;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
    
    public static void keyboardShow(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.showSoftInput(view, 0);
		}
    }
    
    public static void keyboardHide(Activity activity) {
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
	
	/**
	 * 判断email格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
	/**
	 * 判断是否是电话号码
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}d{1}-?d{8}$)|"
				+ "(^0[3-9] {1}d{2}-?d{7,8}$)|"
								+ "(^0[1,2]{1}d{1}-?d{8}-(d{1,4})$)|"
				+ "(^0[3-9]{1}d{2}-? d{7,8}-(d{1,4})$))";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	public static String formatHundredMillionCNY(double value) {
		if (value == 0) {
			return "0.00亿元";
		}
		
		DecimalFormat df = new DecimalFormat("0.##");
		String ret = df.format(value / 100000000.0f);
		return ret + "亿元";
	}

	
	public static String formatTenThousandCNY(double value) {
		if (value == 0) {
			return "0.00万元";
		}
		
		if (Math.abs(value) >= 100000000.0f) {
			return formatHundredMillionCNY(value);
		}
		
		DecimalFormat df = new DecimalFormat("0.##");
		String ret = df.format(value / 10000.0f);
		return ret + "万元";
	}
	
	public static String formatCNY(double value) {
		if (value == 0) {
			return "0.00元";
		}
		
		if (Math.abs(value) >= 1000000.0f) {
			return formatTenThousandCNY(value);
		}
		
		DecimalFormat df = new DecimalFormat("0.##");
		String ret = df.format(value);
		return ret + "元";
	}
	
	public static int getRatioColor(int baseColor, int targetColor, float ratio) {
		int alpha = (int) (Color.alpha(baseColor) + (Color.alpha(targetColor) - Color.alpha(baseColor)) * ratio);
		int red = (int) (Color.red(baseColor) + (Color.red(targetColor) - Color.red(baseColor)) * ratio);
		int green = (int) (Color.green(baseColor) + (Color.green(targetColor) - Color.green(baseColor)) * ratio);
		int blue = (int) (Color.blue(baseColor) + (Color.blue(targetColor) - Color.blue(baseColor)) * ratio);
		
		return Color.argb(alpha, red, green, blue);
	}


}