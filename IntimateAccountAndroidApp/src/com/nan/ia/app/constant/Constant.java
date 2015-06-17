package com.nan.ia.app.constant;

public class Constant {
	// 分类-支出
	public static final String CATEGORY_EXPEND = "支出";
	// 分类-收入
	public static final String CATEGORY_INCOME = "收入";
	
	// 验证码长度
	public static final int VF_CODE_LENGTH = 4;
	// 空账本ID
	public static final int NULL_ACCOUNT_BOOK_ID = -1;
	// 默认创建账本的初始ID
	public static final int DEFAULT_ACCOUNT_BOOK_ID = -2;
	// 本地未登录账户ID
	public static final int UNLOGIN_USER_ID = -1;
	// 未登录账户
	public static final int ACCOUNT_TYPE_UNLOGIN = -1;
	// 邮箱账户
	public static final int ACCOUNT_TYPE_MAIL = 1;
	// 手机用户
	public static final int ACCOUNT_TYPE_PHONE = 2;
	
	// 变更标记
	public static final String CHANGE_TYE_CURRENT_ACCOUNT_BOOK = "CHANGE_TYE_CURRENT_ACCOUNT_BOOK";
	public static final String CHANGE_TYE_DO_SYNC_DATA = "CHANGE_TYE_DO_SYNC_DATA";
}