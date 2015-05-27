/**
 * @ClassName:     AppDataStoreHelper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月26日 
 */

package com.nan.ia.app.data;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.nan.ia.app.utils.LogUtils;

public class AppDataStoreHelper {
	static SharedPreferences sPreferences;
	static Gson gson;
	
	public static void init(Context context) {
		sPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
		gson = new Gson();
	}
	
	// 存储方法
	public static void store(String key, String value) {
		Editor editor = sPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void store(String key, Object value) {
		Editor editor = sPreferences.edit();
		editor.putString(key, gson.toJson(value));
		editor.commit();
	}
	
	public static void store(String key, int value) {
		Editor editor = sPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void store(String key, long value) {
		Editor editor = sPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static void store(String key, boolean value) {
		Editor editor = sPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static void store(String key, float value) {
		Editor editor = sPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	// 获取方法
	public static boolean getBoolean(String key, boolean defValue) {
		return sPreferences.getBoolean(key, defValue);
	}
	
	public static float getFloat(String key, float defValue) {
		return sPreferences.getFloat(key, defValue);
	}
	
	public static int getInt(String key, int defValue) {
		return sPreferences.getInt(key, defValue);
	}
	
	public static long getLong(String key, long defValue) {
		return sPreferences.getLong(key, defValue);
	}
	
	public static String getString(String key, String defValue) {
		return sPreferences.getString(key, defValue);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObject(String key, Class<?> type, T defValue) {
		String json = sPreferences.getString(key, "");
		if (null == json || json.isEmpty()) {
			return defValue;
		}
		
		try {
			T object = (T) gson.fromJson(json, type);
			return object;
		} catch (Exception e) {
			LogUtils.w("AppDataStoreHelper getObject error, key=" + key, e);
		}
		
		return defValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObject(String key, Type type, T defValue) {
		String json = sPreferences.getString(key, "");
		if (null == json || json.isEmpty()) {
			return defValue;
		}
		
		try {
			T object = (T) gson.fromJson(json, type);
			return object;
		} catch (Exception e) {
			LogUtils.w("AppDataStoreHelper getObject error, key=" + key, e);
		}
		
		return defValue;
	}
}
