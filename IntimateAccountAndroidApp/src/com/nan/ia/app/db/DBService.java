package com.nan.ia.app.db;

import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private SQLiteDatabase mDatabase = null;
	
	private static DBService sDbService = null;
	public static DBService getInstance(Context context)
	{
		if(sDbService == null)
		{
			sDbService = new DBService(context);
		}
		
		return sDbService;
	}
	
	private DBService(Context context) {
		mDatabase = DBHelper.getInstance(context).getReadableDatabase();
	}
	
	private List<String> getNeedSyncSqlOperate() {
		return null;
	}
	
	public void createAccountBooks(int userId, String name, String description) {
		long currentTimeMillis = System.currentTimeMillis();
		mDatabase.execSQL("INSERT INTO `account_book_tbl` (`name`, `description`, `create_user_id`, `update_time`, `create_time`) VALUES (?, ?, ?, ?, ?);",
				new Object[] { name, description, userId, currentTimeMillis, currentTimeMillis });
	}
	
	private int execOperateSQL(String sql) {
		return 0;
	}
}