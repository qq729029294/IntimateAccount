/**
 * @ClassName:     AppData.java
 * @Description:   应用数据 
 * 
 * @author         weijiangnan create on 2015-5-11
 */

package com.nan.ia.app.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;

import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.entities.AccountBookInfo;
import com.nan.ia.app.entities.AccountInfo;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.UserInfo;

public class AppData {
	// 应用数据
	static boolean init = false;
	static AccountInfo accountInfo = null;
	static int createAccountBookId = Constant.DEFAULT_ACCOUNT_BOOK_ID;
	static int currentAccountBookId = -1;

	static List<AccountBook> accountBooks = new ArrayList<AccountBook>();
	static List<AccountBook> deleteBooks = new ArrayList<AccountBook>();
	static HashMap<Integer, List<Integer>> bookMembersMap =  new HashMap<Integer, List<Integer>>();
	static List<AccountCategory> categories = new ArrayList<AccountCategory>();
	static List<AccountCategory> deleteCategories = new ArrayList<AccountCategory>();
	static List<AccountRecord> deleteRecords = new ArrayList<AccountRecord>();
	static HashMap<Integer, UserInfo> userInfoCache = new HashMap<Integer, UserInfo>();
	
	static long lastSyncDataTime = 0;
	static long lastSyncDataLocalTime = 0;
	
	// 内存数据
	static HashMap<Integer, AccountBookInfo> bookInfoCache = new HashMap<Integer, AccountBookInfo>();

	public static boolean isInit() {
		return init;
	}

	public static void setInit(boolean init) {
		AppData.init = init;

		new Storage("init").store();
	}

	public static int getCreateAccountBookId() {
		return createAccountBookId;
	}

	public static void setCreateAccountBookId(int createAccountBookId) {
		AppData.createAccountBookId = createAccountBookId;

		new Storage("createAccountBookId").store();
	}

	public static AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public static void setAccountInfo(AccountInfo accountInfo) {
		AppData.accountInfo = accountInfo;

		new Storage("accountInfo").store();
	}

	public static int getCurrentAccountBookId() {
		return currentAccountBookId;
	}

	public static void setCurrentAccountBookId(int currentAccountBookId) {
		AppData.currentAccountBookId = currentAccountBookId;

		new Storage("currentAccountBookId").store();
	}

	public static List<AccountBook> getAccountBooks() {
		return accountBooks;
	}

	public static void setAccountBooks(List<AccountBook> accountBooks) {
		AppData.accountBooks = accountBooks;

		new Storage("accountBooks").store();
	}

	public static List<AccountCategory> getCategories() {
		return categories;
	}

	public static void setCategories(List<AccountCategory> categories) {
		AppData.categories = categories;

		new Storage("categories").store();
	}

	public static long getLastSyncDataTime() {
		return lastSyncDataTime;
	}

	public static void setLastSyncDataTime(long lastSyncDataTime) {
		AppData.lastSyncDataTime = lastSyncDataTime;
		
		new Storage("lastSyncDataTime").store();
	}

	public static long getLastSyncDataLocalTime() {
		return lastSyncDataLocalTime;
	}

	public static void setLastSyncDataLocalTime(long lastSyncDataLocalTime) {
		AppData.lastSyncDataLocalTime = lastSyncDataLocalTime;

		new Storage("lastSyncDataLocalTime").store();
	}

	public static List<AccountBook> getDeleteBooks() {
		return deleteBooks;
	}

	public static void setDeleteBooks(List<AccountBook> deleteBooks) {
		AppData.deleteBooks = deleteBooks;

		new Storage("deleteBooks").store();
	}

	public static HashMap<Integer, List<Integer>> getBookMembersMap() {
		return bookMembersMap;
	}

	public static void setBookMembersMap(
			HashMap<Integer, List<Integer>> bookMembersMap) {
		AppData.bookMembersMap = bookMembersMap;
		
		new Storage("bookMembersMap").store();
	}

	public static List<AccountCategory> getDeleteCategories() {
		return deleteCategories;
	}

	public static void setDeleteCategories(
			List<AccountCategory> deleteCategories) {
		AppData.deleteCategories = deleteCategories;

		new Storage("deleteCategories").store();
	}

	public static List<AccountRecord> getDeleteRecords() {
		return deleteRecords;
	}

	public static void setDeleteRecords(List<AccountRecord> deleteRecords) {
		AppData.deleteRecords = deleteRecords;

		new Storage("deleteRecords").store();
	}

	public static HashMap<Integer, UserInfo> getUserInfoCache() {
		return userInfoCache;
	}

	public static void setUserInfoCache(HashMap<Integer, UserInfo> userInfoCache) {
		AppData.userInfoCache = userInfoCache;

		new Storage("userInfoCache").store();
	}
	
	public static HashMap<Integer, AccountBookInfo> getBookInfoCache() {
		return bookInfoCache;
	}

	public static void setBookInfoCache(
			HashMap<Integer, AccountBookInfo> bookInfoCache) {
		AppData.bookInfoCache = bookInfoCache;
	}

	public static void initAppData(Context context) {
		AppDataStoreHelper.init(context);
		readStoreData();
		ResourceMapper.initMap();

		initDefaultData();
	}

	private static void initDefaultData() {
		if (null == accountInfo) {
			accountInfo = new AccountInfo();
			accountInfo.setAccountType(Constant.ACCOUNT_TYPE_UNLOGIN);
			accountInfo.setUserId(Constant.UNLOGIN_USER_ID);
		}
	}

	private static void readStoreData() {
		Class<?> x = AppData.class;
		Field[] fields = x.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			Class<?> fieldClass = field.getType();

			try {
				if (fieldClass.isAssignableFrom(int.class)) {
					field.setInt(null,
							AppDataStoreHelper.getInt(name, field.getInt(null)));
				} else if (fieldClass.isAssignableFrom(long.class)) {
					field.setLong(
							null,
							AppDataStoreHelper.getLong(name,
									field.getLong(null)));
				} else if (fieldClass.isAssignableFrom(boolean.class)) {
					field.setBoolean(
							null,
							AppDataStoreHelper.getBoolean(name,
									field.getBoolean(null)));
				} else if (fieldClass.isAssignableFrom(float.class)) {
					field.setFloat(
							null,
							AppDataStoreHelper.getFloat(name,
									field.getFloat(null)));
				} else if (fieldClass.isAssignableFrom(String.class)) {
					field.set(
							null,
							AppDataStoreHelper.getString(name,
									(String) field.get(null)));
				} else {
					field.set(
							null,
							AppDataStoreHelper.getObject(name,
									field.getGenericType(), field.get(null)));
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	private static void storeAppData() {
		Class<?> x = AppData.class;
		Field[] fields = x.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			storeField(fields[i]);
		}
	}

	private static void storeField(Field field) {
		try {
			String name = field.getName();
			Class<?> fieldClass = field.getType();
			if (fieldClass.isAssignableFrom(int.class)) {
				AppDataStoreHelper.store(name, field.getInt(null));
			} else if (fieldClass.isAssignableFrom(long.class)) {
				AppDataStoreHelper.store(name, field.getLong(null));
			} else if (fieldClass.isAssignableFrom(boolean.class)) {
				AppDataStoreHelper.store(name, field.getBoolean(null));
			} else if (fieldClass.isAssignableFrom(float.class)) {
				AppDataStoreHelper.store(name, field.getFloat(null));
			} else if (fieldClass.isAssignableFrom(String.class)) {
				AppDataStoreHelper.store(name, (String) field.get(null));
			} else {
				AppDataStoreHelper.store(name, field.get(null));
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static void storeField(String FieldName) {
		Class<?> x = AppData.class;

		try {
			Field field = x.getDeclaredField(FieldName);
			storeField(field);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static void beginStore() {
		Storage.beginStore();
	}

	public static void endStore() {
		Storage.endStore();
	}

	public static void store(String fieldName) {
		new Storage(fieldName).store();
	}

	public static class Storage {
		static HashSet<String> sFields = new HashSet<String>();
		static int sBeginStoreCount = 0;
		String mFieldName;

		public Storage(String fieldName) {
			mFieldName = fieldName;
			sFields.add(fieldName);
		}

		/**
		 * 存储
		 */
		void store() {
			if (sBeginStoreCount == 0) {
				AppData.storeField(mFieldName);
				sFields.remove(mFieldName);
			}
		}

		public static void storeCacheFields() {
			while (sFields.iterator().hasNext()) {
				String fieldName = sFields.iterator().next();
				AppData.storeField(fieldName);
				sFields.remove(fieldName);
			}
		}

		public static void beginStore() {
			sBeginStoreCount++;
		}

		public static void endStore() {
			sBeginStoreCount--;

			if (sBeginStoreCount == 0) {
				storeCacheFields();
			}
		}
	}
}