/**
 * @ClassName:     SyncDataResponse.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;
import java.util.Map;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.UserInfo;

public class SyncDataResponseData {
	long lastSyncDataTime;
	
	boolean updateBooks;
	List<AccountBook> books;
	Map<Integer, List<Integer>> bookMembersMap;
	Map<Integer, Integer> newBookIdMap;
	List<UserInfo> relateUserInfos;
	
	boolean updateCategories;
	List<AccountCategory> categories;
	
	List<AccountRecord> newRecords;
	List<AccountRecord> updateRecords;
	List<Integer> deleteRecordIds;
	public long getLastSyncDataTime() {
		return lastSyncDataTime;
	}
	public void setLastSyncDataTime(long lastSyncDataTime) {
		this.lastSyncDataTime = lastSyncDataTime;
	}
	public boolean isUpdateBooks() {
		return updateBooks;
	}
	public void setUpdateBooks(boolean updateBooks) {
		this.updateBooks = updateBooks;
	}
	public List<AccountBook> getBooks() {
		return books;
	}
	public void setBooks(List<AccountBook> books) {
		this.books = books;
	}
	public Map<Integer, List<Integer>> getBookMembersMap() {
		return bookMembersMap;
	}
	public void setBookMembersMap(Map<Integer, List<Integer>> bookMembersMap) {
		this.bookMembersMap = bookMembersMap;
	}
	public Map<Integer, Integer> getNewBookIdMap() {
		return newBookIdMap;
	}
	public void setNewBookIdMap(Map<Integer, Integer> newBookIdMap) {
		this.newBookIdMap = newBookIdMap;
	}
	public boolean isUpdateCategories() {
		return updateCategories;
	}
	public void setUpdateCategories(boolean updateCategories) {
		this.updateCategories = updateCategories;
	}
	public List<AccountCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<AccountCategory> categories) {
		this.categories = categories;
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
	public List<Integer> getDeleteRecordIds() {
		return deleteRecordIds;
	}
	public void setDeleteRecordIds(List<Integer> deleteRecordIds) {
		this.deleteRecordIds = deleteRecordIds;
	}
	public List<UserInfo> getRelateUserInfos() {
		return relateUserInfos;
	}
	public void setRelateUserInfos(List<UserInfo> relateUserInfos) {
		this.relateUserInfos = relateUserInfos;
	}
}
