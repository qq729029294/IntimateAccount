package com.nan.ia.app.biz;

import java.util.List;

import android.widget.Toast;

import com.nan.ia.app.App;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.db.DBService;
import com.nan.ia.app.entities.AccountBook;
import com.nan.ia.app.entities.AccountCategory;
import com.nan.ia.app.entities.AccountItem;
import com.nan.ia.app.widget.CustomToast;

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
		accountBook.setCreateUerId(AppData.getUserLoginInfo().getUserId());
		accountBook.setName(name);
		accountBook.setDescription(description);
		
		long currentTimeMillis = System.currentTimeMillis();
		accountBook.setCreateTime(currentTimeMillis);
		accountBook.setUpdateTime(currentTimeMillis);
		
		// 添加账本数据
		AppData.getAccountBooks().add(accountBook);
	}
	
	public void editAccountBooksDetail(int accountBookId, String name, String description) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				accountBook.setName(name);
				accountBook.setDescription(description);
				accountBook.setUpdateTime(System.currentTimeMillis());
			}
		}
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
	
	// 重新加载数据
	public void reloadDataFromDB() {
		
	}
}