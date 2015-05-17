/**
 * @ClassName:     AccountBookDelete.java
 * @Description:   删除表 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.entities;

import java.util.Date;

public class AccountBookDelete {
	private int accountBookId;
	private int deleteUserId;
	private Date deleteTime;
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
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
