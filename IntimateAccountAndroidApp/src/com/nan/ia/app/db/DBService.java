package com.nan.ia.app.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nan.ia.common.entities.AccountRecord;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.UpdateAppearance;

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
	
	public void createAccountBook(int userId, String name, String description) {
		long currentTimeMillis = System.currentTimeMillis();
		mDatabase.execSQL("INSERT INTO `account_book_tbl` (`name`, `description`, `create_user_id`, `update_time`, `create_time`) VALUES (?, ?, ?, ?, ?);",
				new Object[] { name, description, userId, currentTimeMillis, currentTimeMillis });
	}
	
	public void createAccountRecord(AccountRecord record) {
		long now = System.currentTimeMillis();
		mDatabase.execSQL(
						"INSERT INTO `account_record_tbl` (`account_book_id`, `category`, `water_value`, `description`, `record_time`, `record_user_id`, `create_time`, `update_time`) VALUES"
								+ "(?, ?, ?, ?, ?, ?, ?, ?);",
						new Object[] { record.getAccountBookId(),
								record.getCategory(), record.getWaterValue(),
								record.getDescription(),
								record.getRecordTime().getTime(),
								record.getRecordUserId(), now, now });
	}
	
	public void updateAccountRecord(AccountRecord record) {
		long now = System.currentTimeMillis();
		mDatabase.execSQL("UPDATE `account_record_tbl` SET `account_book_id`=?, `category`=?, `water_value`=?, `description`=?, `record_time`=?, `record_user_id`=?, `create_time`=?, `update_time`=? WHERE `account_record_id`=?",
				new Object[] {
				record.getAccountBookId(),
				record.getCategory(), record.getWaterValue(),
				record.getDescription(),
				record.getRecordTime().getTime(),
				record.getRecordUserId(), record.getCreateTime().getTime(),
				now,
				record.getAccountRecordId()
		});
	}
	
	public void updateAccountRecordsUserId(int oldUserId, int newUserId) {
		mDatabase.execSQL("UPDATE `account_record_tbl` SET `record_user_id`=? WHERE `record_user_id`=?",
				new Object[] { newUserId, oldUserId });
	}
	
	public void deleteAccountRecord(int accountRecordId) {
		mDatabase.execSQL("DELETE FROM `account_record_tbl` WHERE `account_record_id`=?",
				new Object[] {
				accountRecordId
		});
	}
	
	public void deleteAccountRecords(List<Integer> accountRecordIds) {
		mDatabase.beginTransaction();
		
		for (int i = 0; i < accountRecordIds.size(); i++) {
			deleteAccountRecord(accountRecordIds.get(i));
		}
		
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
	}
	
	public void deleteAccountRecordsByLastUpdateTime(long lastUpdateTime) {
		mDatabase.execSQL("DELETE FROM `account_record_tbl` WHERE `update_time`>?",
				new Object[] { lastUpdateTime });
	}
	
	public void insertAccountRecord(AccountRecord record) {
		mDatabase
				.execSQL(
						"INSERT INTO `account_record_tbl` (`account_record_id`, `account_book_id`, `category`, `water_value`, `description`, `record_time`, `record_user_id`, `create_time`, `update_time`) VALUES"
								+ "(?, ?, ?, ?, ?, ?, ?, ?, ?);",
						new Object[] { record.getAccountRecordId(),
								record.getAccountBookId(),
								record.getCategory(), record.getWaterValue(),
								record.getDescription(),
								record.getRecordTime().getTime(),
								record.getRecordUserId(),
								record.getCreateTime().getTime(), record.getUpdateTime().getTime() });
	}
	
	public void insertAccountRecords(List<AccountRecord> records) {
		mDatabase.beginTransaction();
		
		for (int i = 0; i < records.size(); i++) {
			insertAccountRecord(records.get(i));
		}
		
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
	}
	
	public List<AccountRecord> queryMoreAccountRecords(int accountBookId, long beginTime) {
		Cursor cursor = mDatabase
				.rawQuery(
						"SELECT * FROM account_record_tbl WHERE account_book_id = ? AND record_time < ? ORDER BY record_time DESC LIMIT 0,100",
						new String[] { String.valueOf(accountBookId), 
								String.valueOf(beginTime) });
		
		List<AccountRecord> records = new ArrayList<AccountRecord>();
		while (cursor.moveToNext()) {
			AccountRecord record = new AccountRecord();
			record.setAccountRecordId(cursor.getInt(cursor
					.getColumnIndex("account_record_id")));
			record.setAccountBookId(cursor.getInt(cursor
					.getColumnIndex("account_book_id")));
			record.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			record.setWaterValue(cursor.getDouble(cursor
					.getColumnIndex("water_value")));
			record.setDescription(cursor.getString(cursor
					.getColumnIndex("description")));
			record.setRecordTime(new Date(cursor.getLong(cursor
					.getColumnIndex("record_time"))));
			record.setRecordUserId(cursor.getInt(cursor
					.getColumnIndex("record_user_id")));
			record.setCreateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("create_time"))));
			record.setUpdateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("update_time"))));

			records.add(record);
		}
		
		cursor.close();
		
		return records;
	}
	
	public List<AccountRecord> queryNewAccountRecords(long lastSyncTime) {
		Cursor cursor = mDatabase
				.rawQuery(
						"SELECT * FROM account_record_tbl WHERE create_time > ?",
						new String[] { String.valueOf(lastSyncTime) });
		
		List<AccountRecord> records = new ArrayList<AccountRecord>();
		while (cursor.moveToNext()) {
			AccountRecord record = new AccountRecord();
			record.setAccountRecordId(cursor.getInt(cursor
					.getColumnIndex("account_record_id")));
			record.setAccountBookId(cursor.getInt(cursor
					.getColumnIndex("account_book_id")));
			record.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			record.setWaterValue(cursor.getDouble(cursor
					.getColumnIndex("water_value")));
			record.setDescription(cursor.getString(cursor
					.getColumnIndex("description")));
			record.setRecordTime(new Date(cursor.getLong(cursor
					.getColumnIndex("record_time"))));
			record.setRecordUserId(cursor.getInt(cursor
					.getColumnIndex("record_user_id")));
			record.setCreateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("create_time"))));
			record.setUpdateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("update_time"))));

			records.add(record);
		}
		
		cursor.close();
		
		return records;
	}
	
	public List<AccountRecord> queryUpdateAccountRecords(long lastSyncTime) {
		Cursor cursor = mDatabase
				.rawQuery(
						"SELECT * FROM account_record_tbl WHERE create_time < ? AND update_time > ?",
						new String[] { String.valueOf(lastSyncTime), String.valueOf(lastSyncTime) });
		
		List<AccountRecord> records = new ArrayList<AccountRecord>();
		while (cursor.moveToNext()) {
			AccountRecord record = new AccountRecord();
			record.setAccountRecordId(cursor.getInt(cursor
					.getColumnIndex("account_record_id")));
			record.setAccountBookId(cursor.getInt(cursor
					.getColumnIndex("account_book_id")));
			record.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			record.setWaterValue(cursor.getDouble(cursor
					.getColumnIndex("water_value")));
			record.setDescription(cursor.getString(cursor
					.getColumnIndex("description")));
			record.setRecordTime(new Date(cursor.getLong(cursor
					.getColumnIndex("record_time"))));
			record.setRecordUserId(cursor.getInt(cursor
					.getColumnIndex("record_user_id")));
			record.setCreateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("create_time"))));
			record.setUpdateTime(new Date(cursor.getLong(cursor
					.getColumnIndex("update_time"))));

			records.add(record);
		}
		
		cursor.close();
		
		return records;
	}
	
	private int execOperateSQL(String sql) {
		return 0;
	}
}