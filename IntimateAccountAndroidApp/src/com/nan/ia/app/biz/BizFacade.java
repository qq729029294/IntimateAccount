package com.nan.ia.app.biz;

import java.util.HashMap;
import java.util.List;

import com.nan.ia.app.entities.AccountBook;
import com.nan.ia.app.entities.AccountCategory;
import com.nan.ia.app.entities.AccountItem;

public class BizFacade {
	private List<AccountBook> mAccountBooks;
	private HashMap<Integer, List<AccountCategory>> mMapAccountCategories;
	
	private static BizFacade sInstance = null;
	
	public static BizFacade getInstance() {
		if (null != sInstance) {
			return sInstance;
		}
		
		synchronized (BizFacade.class) {
			if (null == sInstance) {
				sInstance = new BizFacade();
			}
		}
		
		return sInstance;
	}
	
	// 账本相关接口
	public AccountBook getCurrentAccountBook() {
		return null;
	}
	
	public void setCurrentAccountBook(int accountBookId) {
		
	}
	
	public List<AccountBook> getAccountBooks() {
		return null;
	}
	
	public AccountBook createAccountBooks(String name, String description) {
		return null;
	}
	
	public void editAccountBooksDetail(int accountBookId, String name, String description) {
		
	}
	
	public void deleteAccountBooks(int accountBookId) {
		
	}
	
	// 类别相关接口
	public List<AccountCategory> getCategories() {
		return null;
	}
	
	public AccountCategory createCategory(AccountCategory superCategory, String category) {
		return null;
	}

	public void editCategory(String category, String newCategory) {
		
	}
	
	public void deleteCategory(String category) {
		
	}
	
	// 账本条目相关接口
	public List<AccountItem> getAccountItems(int accountBookId, long lastCreateTime, int count) {
		return null;
	}

	public AccountItem createAccountItem(AccountCategory category, double waterValue, String description) {
		return null;
	}

	public void editAccountItemDetial(int accountItemId, AccountCategory category, double waterValue, String description) {
		
	}
	
	public void deleteAccountItem(int accountItemId) {
		
	}
	
	// 同步服务器数据
	public void syncServerData() {
		// 获取本地新增的数据信息
	}
}