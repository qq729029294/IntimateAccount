/**
 * @ClassName:     SyncDataRequest.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;

public class SyncDataRequestData extends CommonRequestData {
	long lastSyncDataTime;
	
	List<AccountBook> newBooks;
	List<AccountBook> updateBooks;
	List<AccountBook> deleteBooks;
	
	List<AccountCategory> newCategories;
	List<AccountCategory> deleteCategories;
	
	List<AccountRecord> newRecords;
	List<AccountRecord> updateRecords;
	List<AccountRecord> deleteRecords;
	public long getLastSyncDataTime() {
		return lastSyncDataTime;
	}
	public void setLastSyncDataTime(long lastSyncDataTime) {
		this.lastSyncDataTime = lastSyncDataTime;
	}
	public List<AccountBook> getNewBooks() {
		return newBooks;
	}
	public void setNewBooks(List<AccountBook> newBooks) {
		this.newBooks = newBooks;
	}
	public List<AccountBook> getUpdateBooks() {
		return updateBooks;
	}
	public void setUpdateBooks(List<AccountBook> updateBooks) {
		this.updateBooks = updateBooks;
	}
	public List<AccountCategory> getNewCategories() {
		return newCategories;
	}
	public void setNewCategories(List<AccountCategory> newCategories) {
		this.newCategories = newCategories;
	}
	public List<AccountRecord> getNewRecords() {
		return newRecords;
	}
	public void setNewRecords(List<AccountRecord> newRecords) {
		this.newRecords = newRecords;
	}
	public List<AccountRecord> getUpdateRecords() {
		return updateRecords;
	}
	public void setUpdateRecords(List<AccountRecord> updateRecords) {
		this.updateRecords = updateRecords;
	}
	public List<AccountBook> getDeleteBooks() {
		return deleteBooks;
	}
	public void setDeleteBooks(List<AccountBook> deleteBooks) {
		this.deleteBooks = deleteBooks;
	}
	public List<AccountCategory> getDeleteCategories() {
		return deleteCategories;
	}
	public void setDeleteCategories(List<AccountCategory> deleteCategories) {
		this.deleteCategories = deleteCategories;
	}
	public List<AccountRecord> getDeleteRecords() {
		return deleteRecords;
	}
	public void setDeleteRecords(List<AccountRecord> deleteRecords) {
		this.deleteRecords = deleteRecords;
	}
}
