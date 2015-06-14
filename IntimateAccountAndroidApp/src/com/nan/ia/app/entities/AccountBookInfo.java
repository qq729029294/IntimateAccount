/**
 * @ClassName:     AccountBookInfo.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月14日 
 */

package com.nan.ia.app.entities;

import java.util.List;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;

public class AccountBookInfo {
	AccountBook accountBook;
	AccountBookStatisticalInfo statisticalInfo;
	List<AccountCategory> categories;
	List<AccountCategory> incomeCategories;
	List<AccountCategory> expendCategories;
	
	public AccountBook getAccountBook() {
		return accountBook;
	}
	public void setAccountBook(AccountBook accountBook) {
		this.accountBook = accountBook;
	}
	public AccountBookStatisticalInfo getStatisticalInfo() {
		return statisticalInfo;
	}
	public void setStatisticalInfo(AccountBookStatisticalInfo statisticalInfo) {
		this.statisticalInfo = statisticalInfo;
	}
	public List<AccountCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<AccountCategory> categories) {
		this.categories = categories;
	}
	public List<AccountCategory> getIncomeCategories() {
		return incomeCategories;
	}
	public void setIncomeCategories(List<AccountCategory> incomeCategories) {
		this.incomeCategories = incomeCategories;
	}
	public List<AccountCategory> getExpendCategories() {
		return expendCategories;
	}
	public void setExpendCategories(List<AccountCategory> expendCategories) {
		this.expendCategories = expendCategories;
	}
}
