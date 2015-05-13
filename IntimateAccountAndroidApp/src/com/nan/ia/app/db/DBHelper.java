package com.nan.ia.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "eoecn";
	private static final int DB_VERSION = 1;

	private static DBHelper mdbHelper;
	public static DBHelper getInstance(Context context)
	{
		if(mdbHelper == null)
		{
			mdbHelper = new DBHelper(context);
		}
		
		return mdbHelper;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists hero_info("
		          + "id integer primary key,"
		          + "name varchar,"
		          + "level integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion == newVersion) {
			return;
		}
	}
}
