package com.nan.ia.app.db;

import java.util.List;

import com.nan.ia.app.entities.AccountBook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private DBHelper dbHelper = null;
	
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
		dbHelper = DBHelper.getInstance(context);
	}
	
	private List<String> getNeedSyncSqlOperate() {
		return null;
	}
	
	public int createAccountBooks(String name, String description) {
		return null;
	}
	
	private int execOperateSQL(String sql) {
		
	}
}