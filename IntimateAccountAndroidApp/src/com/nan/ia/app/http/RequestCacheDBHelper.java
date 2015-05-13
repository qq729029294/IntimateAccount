package com.nan.ia.app.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RequestCacheDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "request_cache_db";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase database = null;

	private static RequestCacheDBHelper mdbHelper;
	public static RequestCacheDBHelper getInstance(Context context)
	{
		if(mdbHelper == null)
		{
			mdbHelper = new RequestCacheDBHelper(context);
		}
		
		return mdbHelper;
	}

	private RequestCacheDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private RequestCacheDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS `request_cache` ("
				  + "`id` INT(11) NOT NULL AUTO_INCREMENT,"
				  + "`url` TEXT NULL,"
				  + "`response` TEXT NULL,"
				  + "`update_time` INT(11) NULL,"
				  + "PRIMARY KEY (`id`))");
		
		database = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion == newVersion) {
			return;
		}
	}
	
	public void insertRequestCache(String url, String response) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("url", url);
		contentValues.put("response", response);
		contentValues.put("update_time", System.currentTimeMillis());
		
		Cursor cursor = database.query("request_cache", new String[] { "id" }, "url=?", new String[] { url }, null, null, null);
		if (cursor.moveToFirst()) {
			database.update("request_cache", contentValues, "url=?", new String[] { url });
		} else {
			database.insert("request_cache", null, contentValues);
		}
		
		cursor.close();
	}
	
	public String getRequestCache(String url, long activeBeginTime) {
		String response = "";
		Cursor cursor = database.query("request_cache", new String[] { "response" }, "url=? and update_time>?", new String[] { url, String.valueOf(activeBeginTime) }, null, null, null);
		if (cursor.moveToFirst()) {
			response = cursor.getString(cursor.getColumnIndex("response"));
		}
		
		return response;
	}
	
	public void clearRequestCache() {
		database.delete("request_cache", null, null);
	}
}
