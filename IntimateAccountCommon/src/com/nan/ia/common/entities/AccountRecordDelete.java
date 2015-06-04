/**
 * @ClassName:     AccountRecordDelete.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月4日 
 */

package com.nan.ia.common.entities;

import java.util.Date;

public class AccountRecordDelete {
	private int accountRecordId;
	private int deleteUserId;
	private Date createTime;
	private Date deleteTime;
	
	public int getAccountRecordId() {
		return accountRecordId;
	}
	public void setAccountRecordId(int accountRecordId) {
		this.accountRecordId = accountRecordId;
	}
	public int getDeleteUserId() {
		return deleteUserId;
	}
	public void setDeleteUserId(int deleteUserId) {
		this.deleteUserId = deleteUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
}
