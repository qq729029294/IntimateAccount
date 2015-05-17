/**
 * @ClassName:     AppData.java
 * @Description:   应用数据 
 * 
 * @author         weijiangnan create on 2015-5-11
 */

package com.nan.ia.app.data;

import java.util.List;

import com.nan.ia.app.entities.UserLoginInfo;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountCategoryDelete;

public class AppData {
	static boolean init;
	static boolean login;
	static UserLoginInfo userLoginInfo;
	
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
	}
	public static boolean isLogin() {
		return login;
	}
	public static void setLogin(boolean login) {
		AppData.login = login;
	}
	public static int getCreateAccountBookId() {
		return createAccountBookId;
	}
	public static void setCreateAccountBookId(int createAccountBookId) {
		AppData.createAccountBookId = createAccountBookId;
	}
	public static UserLoginInfo getUserLoginInfo() {
		return userLoginInfo;
	}
	public static void setUserLoginInfo(UserLoginInfo userLoginInfo) {
		AppData.userLoginInfo = userLoginInfo;
	}
	public static int getCurrentAccountBookId() {
		return currentAccountBookId;
	}
	public static void setCurrentAccountBookId(int currentAccountBookId) {
		AppData.currentAccountBookId = currentAccountBookId;
	}
	public static List<AccountBook> getAccountBooks() {
		return accountBooks;
	}
	public static void setAccountBooks(List<AccountBook> accountBooks) {
		AppData.accountBooks = accountBooks;
	}
	public static List<AccountBookDelete> getAccountBookDeletes() {
		return accountBookDeletes;
	}
	public static void setAccountBookDeletes(
			List<AccountBookDelete> accountBookDeletes) {
		AppData.accountBookDeletes = accountBookDeletes;
	}
	public static List<AccountCategory> getAccountCategories() {
		return accountCategories;
	}
	public static void setAccountCategories(List<AccountCategory> accountCategories) {
		AppData.accountCategories = accountCategories;
	}
	public static List<AccountCategoryDelete> getAccountCategoryDeletes() {
		return accountCategoryDeletes;
	}
	public static void setAccountCategoryDeletes(
			List<AccountCategoryDelete> accountCategoryDeletes) {
		AppData.accountCategoryDeletes = accountCategoryDeletes;
	}
	public static long getLastSyncDataServerTime() {
		return lastSyncDataServerTime;
	}
	public static void setLastSyncDataServerTime(long lastSyncDataServerTime) {
		AppData.lastSyncDataServerTime = lastSyncDataServerTime;
	}
	public static long getLastSyncDataLocalTime() {
		return lastSyncDataLocalTime;
	}
	public static void setLastSyncDataLocalTime(long lastSyncDataLocalTime) {
		AppData.lastSyncDataLocalTime = lastSyncDataLocalTime;
	}
}