/**
 * @ClassName:     SyncDataResponse.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

import com.nan.ia.common.entities.AccountBook;

public class SyncDataResponseData {
	long lastSyncDataServerTime;
	boolean updateAccountBooks;
	List<AccountBook> accountBooks;
	
	public long getLastSyncDataServerTime() {
		return lastSyncDataServerTime;
	}
	public void setLastSyncDataServerTime(long lastSyncDataServerTime) {
		this.lastSyncDataServerTime = lastSyncDataServerTime;
	}
	public boolean isUpdateAccountBooks() {
		return updateAccountBooks;
	}
	public void setUpdateAccountBooks(boolean updateAccountBooks) {
		this.updateAccountBooks = updateAccountBooks;
	}
	public List<AccountBook> getAccountBooks() {
		return accountBooks;
	}
	public void setAccountBooks(List<AccountBook> accountBooks) {
		this.accountBooks = accountBooks;
	}
}
