/**
 * @ClassName:     AccountCategoryDelete.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.entities;

import java.util.Date;

public class AccountCategoryDelete {
	private int accountBookId;
	private String category;
	private int deleteUserId;
	private Date deleteTime;
	
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
