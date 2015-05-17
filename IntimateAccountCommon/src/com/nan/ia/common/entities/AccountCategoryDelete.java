/**
 * @ClassName:     AccountCategoryDelete.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.entities;

import java.util.Date;

public class AccountCategoryDelete {
	private int accountCategoryId;
	private int deleteUserId;
	private Date deleteTime;
	public int getAccountCategoryId() {
		return accountCategoryId;
	}
	public void setAccountCategoryId(int accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
	}
	public int getDeleteUserId() {
		return deleteUserId;
	}
	public void setDeleteUserId(int deleteUserId) {
		this.deleteUserId = deleteUserId;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
}
