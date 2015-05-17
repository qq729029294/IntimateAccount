package com.nan.ia.app.biz;

import java.util.Date;
import java.util.List;

import com.nan.ia.app.data.AppData;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountItem;

public class BizFacade {

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
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			if (accountBooks.get(i).getAccountBookId() == AppData.getCurrentAccountBookId()) {
				return accountBooks.get(i);
			}
		}
		
		return null;
	}
	
	public void setCurrentAccountBook(int accountBookId) {
		AppData.setCurrentAccountBookId(accountBookId);
	}
	
	public List<AccountBook> getAccountBooks() {
		return AppData.getAccountBooks();
	}
	
	public boolean checkDuplicationAccountName(String name) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			if (accountBooks.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int obtainNewAccountBookId() {
		AppData.setCreateAccountBookId(AppData.getCreateAccountBookId() - 1);
		return AppData.getCreateAccountBookId() + 1;
	}
	
	public void createAccountBooks(String name, String description) {
		AccountBook accountBook = new AccountBook();
		accountBook.setAccountBookId(obtainNewAccountBookId());
		accountBook.setCreateUserId(AppData.getUserLoginInfo().getUserId());
		accountBook.setName(name);
		accountBook.setDescription(description);
		
		accountBook.setCreateTime(new Date());
		accountBook.setUpdateTime(new Date());
		
		// 添加账本数据
		AppData.getAccountBooks().add(accountBook);
		
		this.trySyncDataToServer();
	}
	
	public void editAccountBooksDetail(int accountBookId, String name, String description) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				accountBook.setName(name);
				accountBook.setDescription(description);
				accountBook.setUpdateTime(new Date());
				
				break;
			}
		}
		
		this.trySyncDataToServer();
	}
	
	public void deleteAccountBooks(int accountBookId) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				accountBooks.remove(accountBook);

				if (AppData.getLastSyncDataLocalTime() > accountBook.getCreateTime().getTime()) {
					// 是在最后一次同步之前创建的，则需要保留到delete中，以同步数据
					AccountBookDelete delete = new AccountBookDelete();
					delete.setAccountBookId(accountBook.getAccountBookId());
					delete.setDeleteUserId(AppData.getUserLoginInfo().getUserId());
					delete.setDeleteTime(new Date());
					
					AppData.getAccountBookDeletes().add(delete);
				}
				
				break;
			}
		}
		
		this.trySyncDataToServer();
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
	
	// 重新加载数据
	public void reloadDataFromDB() {
		
	}
	
	/**
	 * 尝试同步数据
	 */
	public void trySyncDataToServer() {
		
	}
	
	// 同步服务器数据
	public void syncDataToServer() {
		// 获取本地新增的数据信息
	}
}