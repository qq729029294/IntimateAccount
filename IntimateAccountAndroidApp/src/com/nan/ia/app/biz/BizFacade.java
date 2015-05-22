package com.nan.ia.app.biz;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.http.cmd.server.AccountLoginServerCmd;
import com.nan.ia.app.http.cmd.server.RegisterServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyMailServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyVfCodeServerCmd;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountItem;
import com.nan.ia.common.http.cmd.entities.AccountLoginRequestData;
import com.nan.ia.common.http.cmd.entities.AccountLoginResponseData;
import com.nan.ia.common.http.cmd.entities.RegisterRequestData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;
import com.nan.ia.common.http.cmd.entities.VerifyMailRequestData;
import com.nan.ia.common.http.cmd.entities.VerifyVfCodeRequestData;

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
	
	// 用户相关接口
	
	
	/**
	 * 验证邮箱
	 * @param mail
	 */
	public ServerResponse<Object> verifyMail(Context context, String mail) {
		VerifyMailRequestData requestData = new VerifyMailRequestData();
		requestData.setMail(mail);
		return new VerifyMailServerCmd().send(context, requestData, false);
	}
	
	/**
	 * 验证验证码
	 * @param context
	 * @param mail
	 * @param vfCode
	 * @return
	 */
	public ServerResponse<Object> verifyVfCode(Context context, String username, int accountType, int vfCode) {
		VerifyVfCodeRequestData requestData = new VerifyVfCodeRequestData();
		requestData.setUsername(username);
		requestData.setAccountType(accountType);
		requestData.setVfCode(vfCode);
		return new VerifyVfCodeServerCmd().send(context, requestData, false);
	}
	
	/**
	 * 注册
	 * @param context
	 * @param username
	 * @param accountType
	 * @param vfCode
	 * @return
	 */
	public ServerResponse<Object> register(Context context, String username, String password, int accountType, int vfCode) {
		RegisterRequestData requestData = new RegisterRequestData();
		requestData.setUsername(username);
		requestData.setPassword(password);
		requestData.setAccountType(accountType);
		requestData.setVfCode(vfCode);
		return new RegisterServerCmd().send(context, requestData, false);
	}
	
	/**
	 * 账户登录
	 * @param context
	 * @param username
	 * @param password
	 * @param accountType
	 * @param vfCode
	 * @return
	 */
	public ServerResponse<AccountLoginResponseData> accountLogin(Context context, String username, String password) {
		AccountLoginRequestData requestData = new AccountLoginRequestData();
		requestData.setUsername(username);
		requestData.setPassword(password);
		requestData.setAccountType(Constant.ACCOUNT_TYPE_MAIL);
		return new AccountLoginServerCmd().send(context, requestData, false);
	}
}