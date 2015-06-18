package com.nan.ia.app.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.View;

import com.nan.ia.app.App;
import com.nan.ia.app.R;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.db.DBService;
import com.nan.ia.app.entities.AccountBookStatisticalInfo;
import com.nan.ia.app.entities.AccountBookInfo;
import com.nan.ia.app.entities.AccountInfo;
import com.nan.ia.app.http.HttpUtil;
import com.nan.ia.app.http.cmd.server.AccountLoginServerCmd;
import com.nan.ia.app.http.cmd.server.AgreeInviteMemberServerCmd;
import com.nan.ia.app.http.cmd.server.InviteMemberServerCmd;
import com.nan.ia.app.http.cmd.server.PullAccountBooksServerCmd;
import com.nan.ia.app.http.cmd.server.PullMsgsServerCmd;
import com.nan.ia.app.http.cmd.server.PullUserInfosServerCmd;
import com.nan.ia.app.http.cmd.server.RegisterServerCmd;
import com.nan.ia.app.http.cmd.server.SyncDataServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyMailServerCmd;
import com.nan.ia.app.http.cmd.server.VerifyVfCodeServerCmd;
import com.nan.ia.app.ui.AccountBookEditActivity;
import com.nan.ia.app.ui.LoginActivity;
import com.nan.ia.app.ui.AccountBookEditActivity.Type;
import com.nan.ia.app.utils.MainThreadExecutor;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomDialogBuilder;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.InviteMemberInfo;
import com.nan.ia.common.entities.UserInfo;
import com.nan.ia.common.http.cmd.entities.AccountLoginRequestData;
import com.nan.ia.common.http.cmd.entities.AccountLoginResponseData;
import com.nan.ia.common.http.cmd.entities.AgreeInviteMemberRequestData;
import com.nan.ia.common.http.cmd.entities.InviteMemberRequestData;
import com.nan.ia.common.http.cmd.entities.NullResponseData;
import com.nan.ia.common.http.cmd.entities.PullAccountBooksRequestData;
import com.nan.ia.common.http.cmd.entities.PullAccountBooksResponseData;
import com.nan.ia.common.http.cmd.entities.PullMsgsRequestData;
import com.nan.ia.common.http.cmd.entities.PullMsgsResponseData;
import com.nan.ia.common.http.cmd.entities.PullUserInfosRequestData;
import com.nan.ia.common.http.cmd.entities.PullUserInfosResponseData;
import com.nan.ia.common.http.cmd.entities.RegisterRequestData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;
import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.http.cmd.entities.SyncDataResponseData;
import com.nan.ia.common.http.cmd.entities.VerifyMailRequestData;
import com.nan.ia.common.http.cmd.entities.VerifyVfCodeRequestData;
import com.nan.ia.common.utils.BoolResult;

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
		
		checkAndCreateDefaultInfo();
		
		// 统计账本信息
		for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
			reloadAccountBookInfo(AppData.getAccountBooks().get(i).getAccountBookId());
		}
		
		// 如果是Wi-Fi开启，并且已经登录的情况，默认同步数据
		if (AppData.getAccountInfo().getAccountType() != Constant.ACCOUNT_TYPE_UNLOGIN &&
				HttpUtil.isWifiDataEnable(App.getInstance())) {
			markChange(Constant.CHANGE_TYE_USER);
		}
		
		// 启动，标记账本变化
		markChange(Constant.CHANGE_TYE_CURRENT_ACCOUNT_BOOK);
	}
	
	private void checkAndCreateDefaultInfo() {
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
		return createAccountBook("生活账本", "记录生活中的点滴");
	}
	
	/**
	 * 给账本创建默认分类
	 * @param accountBookId
	 */
	private void createDefaultCategory(int accountBookId) {
		// 开始存储
		AppData.beginStore();
		
		createCategory(accountBookId, "", "支出", ResourceMapper.icon_category_45);
		createCategory(accountBookId, "支出", "餐饮", ResourceMapper.icon_category_32);
		createCategory(accountBookId, "支出", "交通", ResourceMapper.icon_category_2);
		createCategory(accountBookId, "支出", "购物", ResourceMapper.icon_category_34);
		createCategory(accountBookId, "支出", "水果零食", ResourceMapper.icon_category_41);
		createCategory(accountBookId, "支出", "酒水饮料", ResourceMapper.icon_category_44);
		createCategory(accountBookId, "支出", "居家", ResourceMapper.icon_category_37);
		createCategory(accountBookId, "支出", "手机通讯", ResourceMapper.icon_category_40);
		createCategory(accountBookId, "支出", "报销账", ResourceMapper.icon_category_31);
		createCategory(accountBookId, "支出", "借出", ResourceMapper.icon_category_36);
		createCategory(accountBookId, "支出", "娱乐", ResourceMapper.icon_category_47);
		createCategory(accountBookId, "支出", "淘宝", ResourceMapper.icon_category_43);
		createCategory(accountBookId, "支出", "人情礼物", ResourceMapper.icon_category_39);
		createCategory(accountBookId, "支出", "医疗教育", ResourceMapper.icon_category_46);
		createCategory(accountBookId, "支出", "书籍", ResourceMapper.icon_category_42);
		createCategory(accountBookId, "支出", "美容运动", ResourceMapper.icon_category_38);
		createCategory(accountBookId, "支出", "宠物", ResourceMapper.icon_category_33);
		
		createCategory(accountBookId, "", "收入", ResourceMapper.icon_category_21);
		createCategory(accountBookId, "收入", "工资", ResourceMapper.icon_category_21);
		createCategory(accountBookId, "收入", "奖金", ResourceMapper.icon_category_23);
		createCategory(accountBookId, "收入", "外快兼职", ResourceMapper.icon_category_24);
		createCategory(accountBookId, "收入", "借入", ResourceMapper.icon_category_25);
		createCategory(accountBookId, "收入", "投资收入", ResourceMapper.icon_category_29);
		createCategory(accountBookId, "收入", "红包", ResourceMapper.icon_category_22);
		createCategory(accountBookId, "收入", "零花钱", ResourceMapper.icon_category_26);
		createCategory(accountBookId, "收入", "生活费", ResourceMapper.icon_category_28);
		createCategory(accountBookId, "收入", "其他", ResourceMapper.icon_category_27);
		
		AppData.endStore();
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
		
		// 标志更新
		markChange(Constant.CHANGE_TYE_CURRENT_ACCOUNT_BOOK);
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
	
	public AccountBook createAccountBook(String name, String description) {
		AccountBook accountBook = new AccountBook();
		accountBook.setAccountBookId(allocateNewAccountBookId());
		accountBook.setCreateUserId(AppData.getAccountInfo().getUserId());
		accountBook.setName(name);
		accountBook.setDescription(description);
		
		accountBook.setCreateTime(new Date());
		accountBook.setUpdateTime(new Date());
		
		// 开始存储
		AppData.beginStore();
		
		// 添加账本数据
		AppData.getAccountBooks().add(accountBook);
		// 保存数据
		AppData.setAccountBooks(AppData.getAccountBooks());
		// 给该账本创建默认分类
		createDefaultCategory(accountBook.getAccountBookId());
		
		// 结束存储
		AppData.endStore();
		
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
		
		// 更新标志
		markBookChange(accountBookId);
		
		return accountBook;
	}
	
	public void deleteAccountBook(int accountBookId) {
		List<AccountBook> accountBooks = AppData.getAccountBooks();
		for (int i = 0; i < accountBooks.size(); i++) {
			AccountBook accountBook = accountBooks.get(i);
			if (accountBookId == accountBooks.get(i).getAccountBookId()) {
				accountBooks.remove(accountBook);
				// 保存数据
				AppData.beginStore();
				AppData.setAccountBooks(accountBooks);
				
				if (AppData.getLastSyncDataTime() > accountBook.getCreateTime().getTime()) {
					// 是在最后一次同步之前创建的，则需要保留到delete中，以同步数据
					AppData.getDeleteBooks().add(accountBook);
					AppData.setDeleteBooks(AppData.getDeleteBooks());
				}
				
				AppData.endStore();
				
				break;
			}
		}
	}
	
	public AccountBookInfo getAccountBookInfo(int accountBookId) {
		if (applyBookChange(accountBookId, "getAccountBookInfo")) {
			reloadAccountBookInfo(accountBookId);
		}
		
		if (!AppData.getBookInfoCache().containsKey(accountBookId)) {
			reloadAccountBookInfo(accountBookId);
		}
		
		return AppData.getBookInfoCache().get(accountBookId);
	}
	
	public void reloadAccountBookInfo(int accountBookId) {
		AccountBookInfo info = new AccountBookInfo();
		info.setAccountBook(getAccountBookById(accountBookId));
		info.setCategories(AppData.getCategories());
		
		info.setExpendCategories(getSubCategories(accountBookId, Constant.CATEGORY_EXPEND));
		info.getExpendCategories().add(getCategory(accountBookId, Constant.CATEGORY_EXPEND));
		
		info.setIncomeCategories(getSubCategories(accountBookId, Constant.CATEGORY_INCOME));
		info.getIncomeCategories().add(getCategory(accountBookId, Constant.CATEGORY_INCOME));
		
		AccountBookStatisticalInfo statisticalInfo = new AccountBookStatisticalInfo();
		statisticalInfo.setExpend(DBService.getInstance(App.getInstance())
				.sumWaterValueByIdAndCategories(accountBookId, info.getExpendCategories()));
		statisticalInfo.setIncome(DBService.getInstance(App.getInstance())
				.sumWaterValueByIdAndCategories(accountBookId, info.getIncomeCategories()));
		statisticalInfo.setBalance(statisticalInfo.getExpend() + statisticalInfo.getIncome());
		
		info.setStatisticalInfo(statisticalInfo);
		
		// 成员用户信息
		List<UserInfo> memberUserInfos = new ArrayList<UserInfo>();
		List<Integer> memberUserIds = AppData.getBookMembersMap().get(accountBookId);
		for (int i = 0; null != memberUserIds && i < memberUserIds.size(); i++) {
			memberUserInfos.add(obtainUserInfo(null, memberUserIds.get(i), false));
		}
		info.setMemberUserInfos(memberUserInfos);
		
		// 加入到缓存中
		AppData.getBookInfoCache().put(accountBookId, info);
	}
	
	// 类别相关接口
	public List<AccountCategory> getCategories() {
		return null;
	}
	
	public boolean checkDuplicationCategory(String category) {
		List<AccountCategory> accountCategories = AppData.getCategories();
		for (int i = 0; i < accountCategories.size(); i++) {
			if (accountCategories.get(i).getCategory().equals(category)) {
				return true;
			}
		}
		
		return false;
	}
	
	public AccountCategory createCategory(int accountBookId, String superCategory, String category, String icon) {
		AccountCategory accountCategory = new AccountCategory();
		accountCategory.setAccountBookId(accountBookId);
		accountCategory.setCategory(category);
		accountCategory.setIcon(icon);
		accountCategory.setSuperCategory(superCategory);
		accountCategory.setCreateTime(new Date());
		accountCategory.setUpdateTime(new Date());
		
		// 添加分类数据
		AppData.getCategories().add(accountCategory);
		// 保存数据
		AppData.setCategories(AppData.getCategories());
		
		return accountCategory;
	}
	
	public AccountCategory getCategory(int accountBookId, String category) {
		for (int i = 0; i < AppData.getCategories().size(); i++) {
			AccountCategory accountCategory = AppData.getCategories().get(i);
			if (accountBookId == accountCategory.getAccountBookId() && accountCategory.getCategory().equals(category)) {
				return AppData.getCategories().get(i);
			}
		}
		
		return null;
	}
	
	public List<AccountCategory> getSubCategories(int accountBookId, String category) {
		List<AccountCategory> subAccountCategories = new ArrayList<AccountCategory>();
		for (int i = 0; i < AppData.getCategories().size(); i++) {
			AccountCategory accountCategory = AppData.getCategories().get(i);
			if (accountBookId == accountCategory.getAccountBookId() && accountCategory.getSuperCategory().equals(category)) {
				subAccountCategories.add(AppData.getCategories().get(i));
			}
		}
		
		return subAccountCategories;
	}
	
	public AccountCategory getRootCategory(int accountBookId, String category) {
		AccountCategory accountCategory = getCategory(accountBookId, category);
		if (accountCategory == null || accountCategory.getSuperCategory() == null || accountCategory.getSuperCategory().isEmpty()) {
			return accountCategory;
		} else {
			return getRootCategory(accountBookId, accountCategory.getSuperCategory());
		}
	}

	public AccountCategory editCategory(int accountBookId, String category, String newCategory) {
		AccountCategory accountCategory = getCategory(accountBookId, category);
		if (null != accountCategory) {
			// 修改分类
			accountCategory.setCategory(newCategory);
						
			// 修改子分类的父分类
			for (int i = 0; i < AppData.getCategories().size(); i++) {
				if (AppData.getCategories().get(i).getSuperCategory().equals(category)) {
					AppData.getCategories().get(i).setSuperCategory(newCategory);
				}
			}
			
			// 保存数据
			AppData.setCategories(AppData.getCategories());
		}
		
		return accountCategory;
	}
	
	public void deleteCategory(int accountBookId, String category) {
		AccountCategory accountCategory = getCategory(accountBookId, category);
		if (null == accountCategory) {
			return;
		}

		// 删除分类
		AppData.getCategories().remove(accountCategory);

		// 保存数据
		AppData.beginStore();
		AppData.setCategories(AppData.getCategories());

		if (accountCategory.getCreateTime().getTime() < AppData
				.getLastSyncDataTime()) {
			// 最后一次同步数据之前创建的分类，需要保存到删除对象中
			AppData.getDeleteCategories().add(accountCategory);
			AppData.setDeleteCategories(AppData.getDeleteCategories());
		}

		AppData.endStore();
	}
	
	// 账本条目相关接口
	public List<AccountRecord> getMoreAccountRecords(int accountBookId, long beginTime) {
		return DBService.getInstance(App.getInstance()).queryMoreAccountRecords(accountBookId, beginTime);
	}

	public void createAccountRecord(AccountRecord record) {
		DBService.getInstance(App.getInstance()).createAccountRecord(record);
		
		// 更新标志
		markBookChange(record.getAccountBookId());
	}

	public void editAccountRecord(AccountRecord record) {
		DBService.getInstance(App.getInstance()).updateAccountRecord(record);
		
		// 更新标志
		markBookChange(record.getAccountBookId());
	}
	
	public void deleteAccountRecord(int accountRecordId) {
		DBService.getInstance(App.getInstance()).deleteAccountRecord(accountRecordId);
		
		// 更新标志
		markBookChange(accountRecordId);
	}
	
	// 重新加载数据
	public void reloadDataFromDB() {
		
	}
	
	/**
	 * 尝试同步数据
	 */
	public void trySyncDataToServer() {
		
	}
	
	private boolean isLocalInvalidBook(int accountBookId) {
		if (accountBookId == Constant.DEFAULT_ACCOUNT_BOOK_ID) {
			// 是默认账本
			if (DBService.getInstance(App.getInstance()).queryAccountCount(accountBookId) == 0) {
				// 无效的账本
				return true;
			}
		}
		
		return false;
	}
	
	private BoolResult<SyncDataRequestData> buildSyncDataRequestData(Context context) {
		DBService dbService = DBService.getInstance(App.getInstance());
		long lastSyncTime = AppData.getLastSyncDataTime();
		
		// 拉取服务器账本
		ServerResponse<PullAccountBooksResponseData> pullAccountBooksResponse = pullAccountBooks(context);
		if (pullAccountBooksResponse.getRet() != ServerErrorCode.RET_SUCCESS) {
			return BoolResult.False();
		}
		List<AccountBook> serverBooks = pullAccountBooksResponse.getData().getBooks();
		
		// 计算默认账本是否有效账本
		int invalidBookId = Constant.NULL_ACCOUNT_BOOK_ID;
		if (getAccountBookById(Constant.DEFAULT_ACCOUNT_BOOK_ID) != null
				&& isLocalInvalidBook(Constant.DEFAULT_ACCOUNT_BOOK_ID) 
				&& serverBooks.size() > 0) {
			// 本地默认账本是无效的账本
			invalidBookId = Constant.DEFAULT_ACCOUNT_BOOK_ID;
			
		}
		
		// 获得需要同步的账本
		List<AccountBook> newBooks = new ArrayList<AccountBook>();		// 新建账本
		List<AccountBook> updateBooks = new ArrayList<AccountBook>();	// 更新账本
		List<AccountBook> deleteBooks = AppData.getDeleteBooks();		// 删除账本
		for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
			AccountBook accountBook = AppData.getAccountBooks().get(i);
			if (accountBook.getCreateTime().getTime() > lastSyncTime) {
				if (invalidBookId == accountBook.getAccountBookId()) {
					// 是本地无效账本，并且服务器上有账本，则不添加此账本
					continue;
				}
				
				// 本地新建的账本
				newBooks.add(accountBook);
			} else if (accountBook.getUpdateTime().getTime() > lastSyncTime) {
				// 更新的装备
				updateBooks.add(accountBook);
			}
		}
		
		// 检查本地账本与服务器重名问题
		if (checkDupliBookNameWithServer(context, newBooks, serverBooks)) {
			return BoolResult.False();
		}
		
		// 获得需要同步的类别数据
		List<AccountCategory> newCategories = new ArrayList<AccountCategory>();		// 新建类别
		List<AccountCategory> updateCategories = new ArrayList<AccountCategory>();	// 更新类别
		List<AccountCategory> deleteCategories = AppData.getDeleteCategories(); 	// 删除的类别
		for (int i = 0; i < AppData.getCategories().size(); i++) {
			AccountCategory category = AppData.getCategories().get(i);
			if (category.getCreateTime().getTime() > lastSyncTime) {
				if (invalidBookId == category.getAccountBookId()) {
					// 是本地无效账本，则不添加此账本的分类
					continue;
				}
				
				// 本地新建类别
				newCategories.add(category);
			} else if (category.getUpdateTime().getTime() > lastSyncTime) {
				// 本地更新类别
				updateCategories.add(category);
			}
		}
		
		// 获得需要同步的账本记录
		List<AccountRecord> newRecords = dbService.queryNewAccountRecords(lastSyncTime);	   	// 新建记录
		List<AccountRecord> updateRecords = dbService.queryUpdateAccountRecords(lastSyncTime); 	// 更新记录
		List<AccountRecord> deleteRecords = AppData.getDeleteRecords();							// 删除的记录
		
		SyncDataRequestData requestData = new SyncDataRequestData();
		requestData.setLastSyncDataTime(AppData.getLastSyncDataTime());
		requestData.setNewBooks(newBooks);
		requestData.setUpdateBooks(updateBooks);
		requestData.setDeleteBooks(deleteBooks);
		
		requestData.setNewCategories(newCategories);
		requestData.setDeleteCategories(deleteCategories);
		
		requestData.setNewRecords(newRecords);
		requestData.setUpdateRecords(updateRecords);
		requestData.setDeleteRecords(deleteRecords);
		
		return BoolResult.True(requestData);
	}
	
	public void syncDataFromServer(SyncDataResponseData responseData) {
		DBService dbService = DBService.getInstance(App.getInstance());
		long lastSyncDataTime = AppData.getLastSyncDataTime();
		
		AppData.beginStore();
		
		if (responseData.isUpdateBooks()) {
			// 更新账本
			AppData.setAccountBooks(responseData.getBooks());
			
			// 如果当前账本是新账本，映射为新账本ID
			if (responseData.getNewBookIdMap().containsKey(AppData.getCurrentAccountBookId())) {
				AppData.setCurrentAccountBookId(responseData.getNewBookIdMap().get(AppData.getCurrentAccountBookId()));
			}
			
			// 更新账本成员信息
			AppData.setBookMembersMap((HashMap<Integer, List<Integer>>) responseData.getBookMembersMap());
			
			// 更新相关用户信息
			for (int i = 0; i < responseData.getRelateUserInfos().size(); i++) {
				UserInfo userInfo = responseData.getRelateUserInfos().get(i);
				AppData.getUserInfoCache().put(userInfo.getUserId(), userInfo);
			}
			AppData.setUserInfoCache(AppData.getUserInfoCache());

			boolean isInvalidBook = false;
			// 当前账本是无效的账本，择选择第一个账本
			for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
				if (AppData.getAccountBooks().get(i).getAccountBookId()
						== AppData.getCurrentAccountBookId()) {
					isInvalidBook = true;
					break;
				}
			}
			
			if (!isInvalidBook) {
				// 不是有效的，选择第一个
				AppData.setCurrentAccountBookId(AppData.getAccountBooks().get(0).getAccountBookId());
			}
		}
		
		if (responseData.isUpdateCategories()) {
			// 更新分类
			AppData.setCategories(responseData.getCategories());
		}
		
		// 删除最后一次更新后的本地账本记录
		dbService.deleteAccountRecordsByLastUpdateTime(lastSyncDataTime);
		// 插入新/修改记录
		List<AccountRecord> insertRecords = responseData.getNewRecords();
		insertRecords.addAll(responseData.getUpdateRecords());
		if (insertRecords.size() > 0) {
			dbService.insertAccountRecords(insertRecords);
		}
		
		// 更新需删除记录
		if (responseData.getDeleteRecordIds().size() > 0) {
			dbService.deleteAccountRecords(responseData.getDeleteRecordIds());
		}
		
		// 更新最后更新时间
		AppData.setLastSyncDataLocalTime(System.currentTimeMillis());
		AppData.setLastSyncDataTime(responseData.getLastSyncDataTime());
		
		AppData.endStore();
		
		// 标志更新
		for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
			markBookChange(AppData.getAccountBooks().get(i).getAccountBookId());
		}
	}
	
	// 同步服务器数据
	public void syncDataToServer(Context context) {
		BoolResult<SyncDataRequestData> result = buildSyncDataRequestData(context);
		if (result.isFalse()) {
			return;
		}
		
		SyncDataRequestData requestData = result.result();
		SyncDataServerCmd cmd = new SyncDataServerCmd();
		ServerResponse<SyncDataResponseData> response = cmd.send(App.getInstance(), requestData, false);
		if (response.getRet() == ServerErrorCode.RET_SUCCESS) {
			syncDataFromServer(response.getData());
		}
	}
	
	public boolean checkDupliBookNameWithServer(final Context context,
			List<AccountBook> localBooks, List<AccountBook> serverBooks) {
		for (int i = 0; i < localBooks.size(); i++) {
			for (int j = 0; j < serverBooks.size(); j++) {
				final AccountBook localBook = localBooks.get(i);
				AccountBook serverBook = serverBooks.get(i);
				
				if (localBook.getAccountBookId() != serverBook.getAccountBookId() &&
						localBook.getName().equals(serverBook.getName())) {
					
					// 有重名的，弹出提示去修改
					MainThreadExecutor.run(new Runnable() {
						
						@Override
						public void run() {
							
							final CustomDialogBuilder dialogBuilder = CustomDialogBuilder.getInstance(context);
							String msg = String.format("本地的账本\"%s\"的名称与服务器上的账本名称有冲突，换一个名字吧", localBook.getName());
							dialogBuilder
							.withButton2Drawable(R.drawable.selector_btn_inverse)
							.withButton2TextColor(context.getResources().getColor(R.color.main_color))
							.withMessage(msg)
							.withButton1Text(context.getString(R.string.btn_ok))
							.withButton2Text(context.getString(R.string.btn_cancel))
							.setButton1Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// 跳转到修改账本名页面
									AccountBookEditActivity.TransData transData = new AccountBookEditActivity.TransData();
									transData.setType(Type.EDIT);
									transData.setAccountBook(localBook);

									Intent intent = new Intent(context, AccountBookEditActivity.class);
									context.startActivity(AccountBookEditActivity.makeTransDataIntent(intent, transData));
									
									dialogBuilder.dismiss();
								}
							}).setButton2Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialogBuilder.dismiss();
								}
							}).show();
						}
					});
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	// 拉取账本信息
	public ServerResponse<PullAccountBooksResponseData> pullAccountBooks(Context context) {
		PullAccountBooksServerCmd cmd = new PullAccountBooksServerCmd();
		ServerResponse<PullAccountBooksResponseData> response = cmd.send(context, new PullAccountBooksRequestData(), false);
		return response;
	}
	
	// 用户相关接口
	/**
	 * 验证邮箱
	 * @param mail
	 */
	public ServerResponse<NullResponseData> verifyMail(Context context, String mail) {
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
	public ServerResponse<NullResponseData> verifyVfCode(Context context, String username, int accountType, int vfCode) {
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
	public ServerResponse<NullResponseData> register(Context context, String username, String password, int accountType, int vfCode) {
		RegisterRequestData requestData = new RegisterRequestData();
		requestData.setUsername(username);
		requestData.setPassword(password);
		requestData.setAccountType(accountType);
		requestData.setVfCode(vfCode);
		return new RegisterServerCmd().send(context, requestData, false);
	}
	
	public boolean checkUsername(String username) {
		if (null == username || username.length() < 6) {
			return false;
		}
		
		if (!Utils.isEmail(username)) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkPassword(String password) {
		if (null == password) {
			return false;
		}
		
		String passwordPattern = "[a-zA-Z0-9_]{6,16}";  
        return Pattern.matches(passwordPattern, password);  
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
		
		ServerResponse<AccountLoginResponseData> response = new AccountLoginServerCmd().send(context, requestData, false);
		if (response.getRet() == ServerErrorCode.RET_SUCCESS) {
			doAfterLoginSuccess(response.getData());
		}
		
		return response;
	}
	
	private void doAfterLoginSuccess(AccountLoginResponseData data) {
		int oldUserId = AppData.getAccountInfo().getUserId();
		int newUserId = data.getUserId();
		
		AccountInfo accountInfo = AppData.getAccountInfo();
		accountInfo.setAccountType(data.getAccountType());
		accountInfo.setUsername(data.getUsername());
		accountInfo.setUserId(data.getUserId());
		accountInfo.setToken(data.getToken());
		AppData.getUserInfoCache().put(data.getUserId(), data.getUserInfo());

		AppData.beginStore();
		// 修改默认的用户id为新的用户id
		changeBooksUserId(oldUserId, newUserId);
		changeRecordsUserId(oldUserId, newUserId);
		
		// 保存到文件中
		AppData.setAccountInfo(accountInfo);
		AppData.setUserInfoCache(AppData.getUserInfoCache());
		AppData.endStore();
	}
	
	/**
	 * 修改账本的用户ID
	 * @param oldUserId
	 * @param newUserId
	 */
	private void changeBooksUserId(int oldUserId, int newUserId) {
		for (int i = 0; i < AppData.getAccountBooks().size(); i++) {
			if (AppData.getAccountBooks().get(i).getCreateUserId() == oldUserId) {
				AppData.getAccountBooks().get(i).setCreateUserId(newUserId);
			}
		}
		
		// 保存到文件中
		AppData.setAccountBooks(AppData.getAccountBooks());
	}
	
	/**
	 * 修改记录中的用户ID
	 * @param oldUserId
	 * @param newUserId
	 */
	private void changeRecordsUserId(int oldUserId, int newUserId) {
		DBService.getInstance(App.getInstance()).updateAccountRecordsUserId(oldUserId, newUserId);
	}
	
	/************************** 变更标记 ******************************************/
	public void markChange(String changeType) {
		ChangeMarkHelper.markChange(changeType);
	}
	
	public void cleanMark(String changeType) {
		ChangeMarkHelper.cleanMark(changeType);
	}
	
	public boolean checkChange(String changeType, String checker) {
		return ChangeMarkHelper.checkChange(changeType, checker);
	}
	
	public boolean applyChange(String changeType, String checker) {
		return ChangeMarkHelper.applyChange(changeType, checker);
	}
	
	public boolean checkBookChange(int accountBookId, String checker) {
		return checkChange("Book" + accountBookId, checker);
	}
	
	public boolean applyBookChange(int accountBookId, String checker) {
		return applyChange("Book" + accountBookId, checker);
	}
	
	public void markBookChange(int accountBookId) {
		markChange("Book" + accountBookId);
	}
	
	public boolean checkLogin(final Activity activity) {
		if (AppData.getAccountInfo().getAccountType() == Constant.ACCOUNT_TYPE_UNLOGIN) {
			// 未登录，先登录
			final CustomDialogBuilder dialogBuilder = CustomDialogBuilder.getInstance(activity);
			String msg = "必须先登录才能使用此功能";
			dialogBuilder
					.withButton2Drawable(R.drawable.selector_btn_inverse)
					.withMessage(msg)
					.withButton2TextColor(activity.getResources().getColor(R.color.main_color))
					.withButton1Text("马上登录")
					.withButton2Text(activity.getString(R.string.btn_cancel))
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							activity.startActivity(new Intent(activity, LoginActivity.class));
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
			
			return false;
		}
		
		return true;
	}
	
	public ServerResponse<NullResponseData> inviteMember(Context context, int accountBookId, String username) {
		InviteMemberRequestData requestData = new InviteMemberRequestData();
		requestData.setAccountBookId(accountBookId);
		requestData.setAccountBookName(getAccountBookById(accountBookId).getName());
		requestData.setInviteeUsername(username);
		InviteMemberServerCmd cmd = new InviteMemberServerCmd();
		return cmd.send(context, requestData, false);
	}
	
	public ServerResponse<NullResponseData> agreeInviteMember(Context context, int accountBookId) {
		AgreeInviteMemberRequestData requestData = new AgreeInviteMemberRequestData();
		requestData.setAccountBookId(accountBookId);
		AgreeInviteMemberServerCmd cmd = new AgreeInviteMemberServerCmd();
		return cmd.send(context, requestData, false);
	}
	
	public ServerResponse<PullUserInfosResponseData> pullUserInfos(Context context, List<Integer> userIds) {
		PullUserInfosRequestData requestData = new PullUserInfosRequestData();
		requestData.setUserIds(userIds);
		PullUserInfosServerCmd cmd = new PullUserInfosServerCmd();
		return cmd.send(context, requestData, false);
	}
	
	public UserInfo obtainUserInfo(Context context, int userId, boolean pullWhenNoFound) {
		UserInfo info = AppData.getUserInfoCache().get(userId);
		if (info == null) {
			if (pullWhenNoFound) {
				List<Integer> userIds = new ArrayList<Integer>();
				userIds.add(userId);
				ServerResponse<PullUserInfosResponseData> response = pullUserInfos(context, userIds);
				if (response.getRet() == ServerErrorCode.RET_SUCCESS && response.getData().getUserInfos().size() > 0) {
					info = response.getData().getUserInfos().get(0);
					AppData.getUserInfoCache().put(userId, info);
				}
			}
			
			if (null == info) {
				info = new UserInfo();
				info.setUserId(userId);
				info.setNickname("未知用户，id" + userId);	
			}
		}
		
		return info;
	}
	
	public boolean pullAndHandleMsgs(Context context) {
		PullMsgsServerCmd cmd = new PullMsgsServerCmd();
		ServerResponse<PullMsgsResponseData> response = cmd.send(context, new PullMsgsRequestData() , false);
		if (response.getRet() == ServerErrorCode.RET_SUCCESS) {
			PullMsgsResponseData responseData = response.getData();
			List<InviteMemberInfo> infos = responseData.getInviteMemberInfos();
			
			if (null != infos && infos.size() > 0) {
				doInviteMembers(context, infos);
			}
			
			return true;
		}
		
		return false;
	}
	
	public void doInviteMembers(final Context context, final List<InviteMemberInfo> infos) {
		if (infos.size() == 0) {
			return;
		}

		final Object wait = new Object();
		for (int i = 0; i < infos.size(); i++) {
			final InviteMemberInfo info = infos.get(i);
			final UserInfo userInfo = obtainUserInfo(context, info.getInviterUserId(), true);
			MainThreadExecutor.run(new Runnable() {
				
				@Override
				public void run() {
					final CustomDialogBuilder dialogBuilder = CustomDialogBuilder
							.getInstance(context);
					String msg = String.format("%s 邀请您一起记录账本\"%s\"，要加入吗？", userInfo.getNickname(), info.getAccountBookName());
					dialogBuilder
							.withMessage(msg)
							.withButton1Text("爽快加入")
							.withButton2Text("残忍拒绝")
							.setButton1Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									new Thread(new Runnable() {
										
										@Override
										public void run() {
											agreeInviteMember(context, info.getAccountBookId());
											
											synchronized(wait) {
												wait.notifyAll();
											}
										}
									}).start();

									dialogBuilder.dismiss();
								}
							}).setButton2Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialogBuilder.dismiss();

								}
							}).show();
					
					dialogBuilder.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							synchronized(wait) {
								wait.notifyAll();
							}
						}
					});
				}
			});				
			
			// 阻塞线程
			synchronized(wait) {
				try {
					wait.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}