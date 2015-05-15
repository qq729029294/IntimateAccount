package com.nan.ia.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "intimate_account_db";
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
		db.execSQL("CREATE TABLE IF NOT EXISTS `account_book_tbl` ("
				+ "`account_book_id` INTEGER,"
				+ "`name` VARCHAR(32) NOT NULL,"
				+ "`description` VARCHAR(255) NULL,"
				+ "`create_user_id` INT NOT NULL,"
				+ "`update_time` TIMESTAMP NOT NULL,"
				+ "`create_time` TIMESTAMP NULL,"
				+ "PRIMARY KEY (`account_book_id`));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `account_category_tbl` ("
				+ "`account_category_id` INTEGER,"
				+ "`account_book_id` INT NOT NULL,"
				+ "`category` VARCHAR(45) NOT NULL,"
				+ "`type` INT NOT NULL,"
				+ "`super_category` VARCHAR(45) NULL,"
				+ "`create_time` TIMESTAMP NOT NULL,"
				+ "PRIMARY KEY (`account_category_id`));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `account_item_tbl` ("
				+ "`account_item_id` INTEGER,"
				+ "`account_book_id` INT NOT NULL,"
				+ "`account_category_id` INT NOT NULL,"
				+ "`create_user_id` INT NOT NULL,"
				+ "`create_time` TIMESTAMP NOT NULL,"
				+ "`water_value` INT NULL DEFAULT 0,"
				+ "`description` VARCHAR(255) NULL,"
				+ "PRIMARY KEY (`account_item_id`))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion == newVersion) {
			return;
		}
	}
}
