package com.nan.ia.app.entities;

import java.util.List;

public class AccountCategory {
	int accountBookId;
	String category;
	String superCategory;
	int createUserId;
	long createTime;
	
	// 子类
	List<AccountCategory> subCategories;
}