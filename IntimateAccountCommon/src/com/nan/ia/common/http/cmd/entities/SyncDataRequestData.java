/**
 * @ClassName:     SyncDataRequest.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;

public class SyncDataRequestData {
	long lastSyncDataServerTime;
	long lastSyncDataLocalTime;
	
	List<AccountBook> newAccountBooks;
	List<AccountBook> updateAccountBooks;
	List<AccountBookDelete> accountBookDeletes;
	
	public long getLastSyncDataServerTime() {
		return lastSyncDataServerTime;
	}
	public void setLastSyncDataServerTime(long lastSyncDataServerTime) {
		this.lastSyncDataServerTime = lastSyncDataServerTime;
	}
	public long getLastSyncDataLocalTime() {
		return lastSyncDataLocalTime;
	}
	public void setLastSyncDataLocalTime(long lastSyncDataLocalTime) {
		this.lastSyncDataLocalTime = lastSyncDataLocalTime;
	}
	public List<AccountBook> getNewAccountBooks() {
		return newAccountBooks;
	}
	public void setNewAccountBooks(List<AccountBook> newAccountBooks) {
		this.newAccountBooks = newAccountBooks;
	}
	public List<AccountBook> getUpdateAccountBooks() {
		return updateAccountBooks;
	}
	public void setUpdateAccountBooks(List<AccountBook> updateAccountBooks) {
		this.updateAccountBooks = updateAccountBooks;
	}
	public List<AccountBookDelete> getAccountBookDeletes() {
		return accountBookDeletes;
	}
	public void setAccountBookDeletes(List<AccountBookDelete> accountBookDeletes) {
		this.accountBookDeletes = accountBookDeletes;
	}
}
