package com.nan.ia.app.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.nan.ia.app.App;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.entities.AccountInfo;
import com.nan.ia.app.http.cmd.server.AccountLoginServerCmd;
import com.nan.ia.app.http.cmd.server.RegisterServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyMailServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyVfCodeServerCmd;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountCategoryDelete;
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

	/**
	 * 初始化应用内容 
	 */
	public void appInit() {
		// 初始化数据
		AppData.initAppData(App.getInstance());
		
		if (AppData.getAccountInfo() == null) {
			// 没有账户信息，创建本地默认账户
			createDefaultAccountInfo();
		}
		
		if (AppData.getAccountBooks().size() == 0) {
			// 没有账本，创建默认账本，并设置为当前账本
			AccountBook accountBook = createDefaultAccountBook();
			AppData.setCurrentAccountBookId(accountBook.getAccountBookId());
		}
		
		// 设置当前账本
		boolean hasCurrentAccountBook = false;
		for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
			if (AppData.getAccountBooks().get(i).getAccountBookId() == AppData.getCurrentAccountBookId()) {
				// 已经有当前账本了
				hasCurrentAccountBook = true;
				break;
			}
		}
		
		if (!hasCurrentAccountBook) {
			// 没有当前账本，则设置第一个账本
			AppData.setCurrentAccountBookId(AppData.getAccountBooks().get(0).getAccountBookId());
		}
	}
	
	private void createDefaultAccountInfo() {
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccountType(Constant.ACCOUNT_TYPE_UNLOGIN);
		accountInfo.setUserId(Constant.UNLOGIN_USER_ID);
		AppData.setAccountInfo(accountInfo);
	}
	
	/**
	 * 创建默认账本
	 */
	private AccountBook createDefaultAccountBook() {
		return createAccountBooks("生活账本（默认）", "记录生活中的点滴");
	}
	
	/**
	 * 给账本创建默认分类
	 * @param accountBookId
	 */
	private void createDefaultCategory(int accountBookId) {
		createCategory(accountBookId, "", "支出");
		createCategory(accountBookId, "支出", "餐饮");
		createCategory(accountBookId, "支出", "交通");
		createCategory(accountBookId, "支出", "购物");
		createCategory(accountBookId, "支出", "娱乐");
		createCategory(accountBookId, "支出", "医教");
		createCategory(accountBookId, "支出", "居家");
		createCategory(accountBookId, "支出", "投资");
		createCategory(accountBookId, "支出", "人情");
		createCategory(accountBookId, "支出", "生意");
		
		createCategory(accountBookId, "", "收入");
		createCategory(accountBookId, "收入", "工资");
		createCategory(accountBookId, "收入", "博彩");
		createCategory(accountBookId, "收入", "拾取");
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
	
	public boolean checkDuplicationAccountBookName(String name) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			if (accountBooks.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int allocateNewAccountBookId() {
		AppData.setCreateAccountBookId(AppData.getCreateAccountBookId() - 1);
		return AppData.getCreateAccountBookId() + 1;
	}
	
	public AccountBook createAccountBooks(String name, String description) {
		AccountBook accountBook = new AccountBook();
		accountBook.setAccountBookId(allocateNewAccountBookId());
		accountBook.setCreateUserId(AppData.getAccountInfo().getUserId());
		accountBook.setName(name);
		accountBook.setDescription(description);
		
		accountBook.setCreateTime(new Date());
		accountBook.setUpdateTime(new Date());
		
		// 添加账本数据
		AppData.getAccountBooks().add(accountBook);
		// 保存数据
		AppData.setAccountBooks(AppData.getAccountBooks());
		
		// 给该账本创建默认分类
		createDefaultCategory(accountBook.getAccountBookId());
		
		return accountBook;
	}
	
	public AccountBook getAccountBookById(int accountBookId) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				return accountBook;
			}
		}
		
		return null;
	}
	
	public AccountBook editAccountBooksDetail(int accountBookId, String name, String description) {
		AccountBook accountBook = getAccountBookById(accountBookId);
		if (null != accountBook) {
			accountBook.setName(name);
			accountBook.setDescription(description);
			accountBook.setUpdateTime(new Date());
			// 保存数据
			AppData.setAccountBooks(AppData.getAccountBooks());
		}
		
		return accountBook;
	}
	
	public void deleteAccountBook(int accountBookId) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				accountBooks.remove(accountBook);
				// 保存数据
				AppData.setAccountBooks(accountBooks);

				if (AppData.getLastSyncDataLocalTime() > accountBook.getCreateTime().getTime()) {
					// 是在最后一次同步之前创建的，则需要保留到delete中，以同步数据
					AccountBookDelete delete = new AccountBookDelete();
					delete.setAccountBookId(accountBook.getAccountBookId());
					delete.setDeleteUserId(AppData.getAccountInfo().getUserId());
					delete.setDeleteTime(new Date());
					
					// 保存数据
					AppData.getAccountBookDeletes().add(delete);
					AppData.setAccountBookDeletes(AppData.getAccountBookDeletes());
				}
				
				break;
			}
		}
	}
	
	// 类别相关接口
	public List<AccountCategory> getCategories() {
		return null;
	}
	
	public boolean checkDuplicationCategory(String category) {
		List<AccountCategory> accountCategories = AppData.getAccountCategories();
		for (int i = 0; i < accountCategories.size(); i++) {
			if (accountCategories.get(i).getCategory().equals(category)) {
				return true;
			}
		}
		
		return false;
	}
	
	public AccountCategory createCategory(int accountBookId, String superCategory, String category) {
		AccountCategory accountCategory = new AccountCategory();
		accountCategory.setAccountBookId(accountBookId);
		accountCategory.setCategory(category);
		accountCategory.setSuperCategory(superCategory);
		accountCategory.setCreateTime(new Date());
		accountCategory.setUpdateTime(new Date());
		
		// 添加分类数据
		AppData.getAccountCategories().add(accountCategory);
		// 保存数据
		AppData.setAccountCategories(AppData.getAccountCategories());
		
		return accountCategory;
	}
	
	public AccountCategory getCategoryById(int accountBookId, String category) {
		for (int i = 0; i < AppData.getAccountCategories().size(); i++) {
			if (AppData.getAccountCategories().get(i).getCategory().equals(category)) {
				return AppData.getAccountCategories().get(i);
			}
		}
		
		return null;
	}
	
	public List<AccountCategory> getSubCategories(int accountBookId, String category) {
		List<AccountCategory> subAccountCategories = new ArrayList<AccountCategory>();
		for (int i = 0; i < AppData.getAccountCategories().size(); i++) {
			if (AppData.getAccountCategories().get(i).getCategory().equals(category)) {
				subAccountCategories.add(AppData.getAccountCategories().get(i));
			}
		}
		
		return subAccountCategories;
	}

	public AccountCategory editCategory(int accountBookId, String category, String newCategory) {
		AccountCategory accountCategory = getCategoryById(accountBookId, category);
		if (null != accountCategory) {
			// 修改分类
			accountCategory.setCategory(newCategory);
						
			// 修改子分类的父分类
			for (int i = 0; i < AppData.getAccountCategories().size(); i++) {
				if (AppData.getAccountCategories().get(i).getSuperCategory().equals(category)) {
					AppData.getAccountCategories().get(i).setSuperCategory(newCategory);
				}
			}
			
			// 保存数据
			AppData.setAccountCategories(AppData.getAccountCategories());
		}
		
		return accountCategory;
	}
	
	public void deleteCategory(int accountBookId, String category) {
		AccountCategory accountCategory = getCategoryById(accountBookId,
				category);
		if (null != accountCategory) {
			return;
		}
		
		// 删除分类
		AppData.getAccountCategories().remove(accountCategory);

		// 删除子分类
		List<AccountCategory> deleteAccountCategories = getSubCategories(
				accountBookId, category);
		AppData.getAccountCategories().removeAll(deleteAccountCategories);

		// 添加到删除对象中
		deleteAccountCategories.add(accountCategory);
		Date now = new Date();
		for (int i = 0; i < deleteAccountCategories.size(); i++) {
			if (deleteAccountCategories.get(i).getCreateTime().getTime() > AppData
					.getLastSyncDataLocalTime()) {
				// 最后一次同步数据之后创建的分类，不需要保存到删除对象中
				continue;
			}

			AccountCategoryDelete delete = new AccountCategoryDelete();
			delete.setAccountBookId(deleteAccountCategories.get(i)
					.getAccountBookId());
			delete.setCategory(deleteAccountCategories.get(i).getCategory());
			delete.setDeleteUserId(AppData.getAccountInfo().getUserId());
			delete.setDeleteTime(now);

			AppData.getAccountCategoryDeletes().add(delete);
		}

		// 保存数据
		AppData.setAccountCategories(AppData.getAccountCategories());
		AppData.setAccountCategoryDeletes(AppData.getAccountCategoryDeletes());
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