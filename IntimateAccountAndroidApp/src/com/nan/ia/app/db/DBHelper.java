package com.nan.ia.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "intimate_account_db";
	private static final int DB_VERSION = 1;

	private static DBHelper sDBHelper;
	public static DBHelper getInstance(Context context)
	{
		if(sDBHelper == null)
		{
			sDBHelper = new DBHelper(context);
		}
		
		return sDBHelper;
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
		db.execSQL("CREATE TABLE IF NOT EXISTS `account_record_tbl` ("
				+ "`account_record_id` INTEGER,"
				+ "`account_book_id` INT NOT NULL,"
				+ "`category` VARCHAR(45) NOT NULL,"
				+ "`water_value` DOUBLE NULL DEFAULT 0,"
				+ "`description` VARCHAR(255) NULL,"
				+ "`record_time` LONG NOT NULL,"
				+ "`record_user_id` INT NOT NULL,"
				+ "`create_time` LONG NOT NULL,"
				+ "`update_time` LONG NOT NULL,"
				+ "PRIMARY KEY (`account_record_id`))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion == newVersion) {
			return;
		}
	}
}
