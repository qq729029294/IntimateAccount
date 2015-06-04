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
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountCategoryDelete;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.AccountRecordDelete;

public class SyncDataRequestData extends BaseRequestData {
	long lastSyncDataTime;
	
	List<AccountBook> newBooks;
	List<AccountBook> updateBooks;
	List<AccountBookDelete> bookDeletes;
	
	List<AccountCategory> newCategories;
	List<AccountCategory> updateCategories;
	List<AccountCategoryDelete> categoryDeletes;
	
	List<AccountRecord> newRecords;
	List<AccountRecord> updateRecords;
	List<AccountRecordDelete> recordDeletes;
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
	public List<AccountBookDelete> getBookDeletes() {
		return bookDeletes;
	}
	public void setBookDeletes(List<AccountBookDelete> bookDeletes) {
		this.bookDeletes = bookDeletes;
	}
	public List<AccountCategory> getNewCategories() {
		return newCategories;
	}
	public void setNewCategories(List<AccountCategory> newCategories) {
		this.newCategories = newCategories;
	}
	public List<AccountCategory> getUpdateCategories() {
		return updateCategories;
	}
	public void setUpdateCategories(List<AccountCategory> updateCategories) {
		this.updateCategories = updateCategories;
	}
	public List<AccountCategoryDelete> getCategoryDeletes() {
		return categoryDeletes;
	}
	public void setCategoryDeletes(List<AccountCategoryDelete> categoryDeletes) {
		this.categoryDeletes = categoryDeletes;
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
	public List<AccountRecordDelete> getRecordDeletes() {
		return recordDeletes;
	}
	public void setRecordDeletes(List<AccountRecordDelete> recordDeletes) {
		this.recordDeletes = recordDeletes;
	}
}
