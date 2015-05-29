/**
 * @ClassName:     AppData.java
 * @Description:   应用数据 
 * 
 * @author         weijiangnan create on 2015-5-11
 */

package com.nan.ia.app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.entities.AccountInfo;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountCategoryDelete;

public class AppData {
	// getter and setter
	static boolean init;
	static AccountInfo accountInfo;
	static int createAccountBookId;
	static int currentAccountBookId;
	
	static List<AccountBook> accountBooks;
	static List<AccountBookDelete> accountBookDeletes;
	static List<AccountCategory> accountCategories;
	static List<AccountCategoryDelete> accountCategoryDeletes;
	
	static long lastSyncDataServerTime;
	static long lastSyncDataLocalTime;
	
	public static boolean isInit() {
		return init;
	}
	public static void setInit(boolean init) {
		AppData.init = init;
		
		AppDataStoreHelper.store("init", init);
	}
	public static int getCreateAccountBookId() {
		return createAccountBookId;
	}
	public static void setCreateAccountBookId(int createAccountBookId) {
		AppData.createAccountBookId = createAccountBookId;
		
		AppDataStoreHelper.store("createAccountBookId", createAccountBookId);
	}
	public static AccountInfo getAccountInfo() {
		return accountInfo;
	}
	public static void setAccountInfo(AccountInfo accountInfo) {
		AppData.accountInfo = accountInfo;
		
		AppDataStoreHelper.store("accountInfo", accountInfo);
	}
	public static int getCurrentAccountBookId() {
		return currentAccountBookId;
	}
	public static void setCurrentAccountBookId(int currentAccountBookId) {
		AppData.currentAccountBookId = currentAccountBookId;
		
		AppDataStoreHelper.store("currentAccountBookId", currentAccountBookId);
	}
	public static List<AccountBook> getAccountBooks() {
		return accountBooks;
	}
	public static void setAccountBooks(List<AccountBook> accountBooks) {
		AppData.accountBooks = accountBooks;
		
		AppDataStoreHelper.store("accountBooks", accountBooks);
	}
	public static List<AccountBookDelete> getAccountBookDeletes() {
		return accountBookDeletes;
	}
	public static void setAccountBookDeletes(
			List<AccountBookDelete> accountBookDeletes) {
		AppData.accountBookDeletes = accountBookDeletes;
		
		AppDataStoreHelper.store("accountBookDeletes", accountBookDeletes);
	}
	public static List<AccountCategory> getAccountCategories() {
		return accountCategories;
	}
	public static void setAccountCategories(List<AccountCategory> accountCategories) {
		AppData.accountCategories = accountCategories;
		
		AppDataStoreHelper.store("accountCategories", accountCategories);
	}
	public static List<AccountCategoryDelete> getAccountCategoryDeletes() {
		return accountCategoryDeletes;
	}
	public static void setAccountCategoryDeletes(
			List<AccountCategoryDelete> accountCategoryDeletes) {
		AppData.accountCategoryDeletes = accountCategoryDeletes;
		
		AppDataStoreHelper.store("accountCategoryDeletes", accountCategoryDeletes);
	}
	public static long getLastSyncDataServerTime() {
		return lastSyncDataServerTime;
	}
	public static void setLastSyncDataServerTime(long lastSyncDataServerTime) {
		AppData.lastSyncDataServerTime = lastSyncDataServerTime;
		
		AppDataStoreHelper.store("lastSyncDataServerTime", lastSyncDataServerTime);
	}
	public static long getLastSyncDataLocalTime() {
		return lastSyncDataLocalTime;
	}
	public static void setLastSyncDataLocalTime(long lastSyncDataLocalTime) {
		AppData.lastSyncDataLocalTime = lastSyncDataLocalTime;
		
		AppDataStoreHelper.store("lastSyncDataLocalTime", lastSyncDataLocalTime);
	}
	
	public static void initAppData(Context context) {
		AppDataStoreHelper.init(context);
		
		readAppData();
	}
	
	public static void readAppData() {
		// 初始化资源映射类
		ResourceMapper.initMap();
		
		init = AppDataStoreHelper.getBoolean("init", false);
		accountInfo = AppDataStoreHelper.getObject("accountInfo", AccountInfo.class, null);
		createAccountBookId = AppDataStoreHelper.getInt("createAccountBookId", Constant.DEFAULT_CREATE_ACCOUNT_BOOK_ID);
		currentAccountBookId = AppDataStoreHelper.getInt("currentAccountBookId", -1);
		
		accountBooks = AppDataStoreHelper.getObject("accountBooks",
				new TypeToken<List<AccountBook>>() {
				}.getType(), new ArrayList<AccountBook>());
		accountBookDeletes = AppDataStoreHelper.getObject("accountBookDeletes",
				new TypeToken<List<AccountBookDelete>>() {
		}.getType(), new ArrayList<AccountBookDelete>());
		accountCategories = AppDataStoreHelper.getObject("accountCategories",
				new TypeToken<List<AccountCategory>>() {
		}.getType(), new ArrayList<AccountCategory>());
		accountCategoryDeletes = AppDataStoreHelper.getObject("accountCategoryDeletes",
				new TypeToken<List<AccountCategoryDelete>>() {
		}.getType(), new ArrayList<AccountCategoryDelete>());
		
		lastSyncDataServerTime = AppDataStoreHelper.getLong("lastSyncDataServerTime", 0);
		lastSyncDataLocalTime = AppDataStoreHelper.getLong("lastSyncDataLocalTime", 0);
	}
	
	public static void readCategoryIconInfo() {
		
	}
}