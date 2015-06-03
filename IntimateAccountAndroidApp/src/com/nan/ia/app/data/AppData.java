/**
 * @ClassName:     AppData.java
 * @Description:   应用数据 
 * 
 * @author         weijiangnan create on 2015-5-11
 */

package com.nan.ia.app.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
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
	static boolean init = false;
	static AccountInfo accountInfo = null;
	static int createAccountBookId = Constant.DEFAULT_CREATE_ACCOUNT_BOOK_ID;
	static int currentAccountBookId = -1;

	static ArrayList<AccountBook> accountBooks = new ArrayList<AccountBook>();
	static ArrayList<AccountBookDelete> accountBookDeletes = new ArrayList<AccountBookDelete>();
	static ArrayList<AccountCategory> accountCategories = new ArrayList<AccountCategory>();
	static ArrayList<AccountCategoryDelete> accountCategoryDeletes = new ArrayList<AccountCategoryDelete>();

	static long lastSyncDataServerTime = 0;
	static long lastSyncDataLocalTime = 0;

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

	public static ArrayList<AccountBook> getAccountBooks() {
		return accountBooks;
	}

	public static void setAccountBooks(ArrayList<AccountBook> accountBooks) {
		AppData.accountBooks = accountBooks;
		
		new Storage("accountBooks").store();
	}

	public static ArrayList<AccountBookDelete> getAccountBookDeletes() {
		return accountBookDeletes;
	}

	public static void setAccountBookDeletes(
			ArrayList<AccountBookDelete> accountBookDeletes) {
		AppData.accountBookDeletes = accountBookDeletes;
		
		new Storage("accountBookDeletes").store();
	}

	public static ArrayList<AccountCategory> getAccountCategories() {
		return accountCategories;
	}

	public static void setAccountCategories(
			ArrayList<AccountCategory> accountCategories) {
		AppData.accountCategories = accountCategories;
		
		new Storage("accountCategories").store();
	}

	public static ArrayList<AccountCategoryDelete> getAccountCategoryDeletes() {
		return accountCategoryDeletes;
	}

	public static void setAccountCategoryDeletes(
			ArrayList<AccountCategoryDelete> accountCategoryDeletes) {
		AppData.accountCategoryDeletes = accountCategoryDeletes;
		
		new Storage("accountCategoryDeletes").store();
	}

	public static long getLastSyncDataServerTime() {
		return lastSyncDataServerTime;
	}

	public static void setLastSyncDataServerTime(long lastSyncDataServerTime) {
		AppData.lastSyncDataServerTime = lastSyncDataServerTime;
		
		new Storage("lastSyncDataServerTime").store();
	}

	public static long getLastSyncDataLocalTime() {
		return lastSyncDataLocalTime;
	}

	public static void setLastSyncDataLocalTime(long lastSyncDataLocalTime) {
		AppData.lastSyncDataLocalTime = lastSyncDataLocalTime;
		
		new Storage("lastSyncDataLocalTime").store();
	}

	public static void initAppData(Context context) {
		AppDataStoreHelper.init(context);

		readStoreData();
		
		ResourceMapper.initMap();
	}

	public static void readStoreData() {
		Class<?> x = AppData.class;
		Field[] fields = x.getDeclaredFields();
		
		ParameterizedType a;

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
					field.set(null, AppDataStoreHelper.getObject(name,
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