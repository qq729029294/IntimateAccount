/**
 * @ClassName:     AppData.java
 * @Description:   应用数据 
 * 
 * @author         weijiangnan create on 2015-5-11
 */

package com.nan.ia.app.data;

import java.util.List;

import com.nan.ia.app.entities.AccountBook;
import com.nan.ia.app.entities.AccountCategory;
import com.nan.ia.app.entities.UserLoginInfo;

public class AppData {
	static boolean init;
	static boolean login;
	static int createAccountBookId;
	static UserLoginInfo userLoginInfo;
	static int currentAccountBookId;
	static List<AccountBook> accountBooks;
	static List<AccountCategory> accountCategories;
	
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
	public static UserLoginInfo getUserLoginInfo() {
		return userLoginInfo;
	}
	public static void setUserLoginInfo(UserLoginInfo userLoginInfo) {
		AppData.userLoginInfo = userLoginInfo;
	}
	public static List<AccountBook> getAccountBooks() {
		return accountBooks;
	}
	public static int getCurrentAccountBookId() {
		return currentAccountBookId;
	}
	public static void setCurrentAccountBookId(int currentAccountBookId) {
		AppData.currentAccountBookId = currentAccountBookId;
	}
	public static List<AccountCategory> getAccountCategories() {
		return accountCategories;
	}
	public static void setAccountCategories(List<AccountCategory> accountCategories) {
		AppData.accountCategories = accountCategories;
	}
	public static void setAccountBooks(List<AccountBook> accountBooks) {
		AppData.accountBooks = accountBooks;
	}
	public static int getCreateAccountBookId() {
		return createAccountBookId;
	}
	public static void setCreateAccountBookId(int createAccountBookId) {
		AppData.createAccountBookId = createAccountBookId;
	}
}