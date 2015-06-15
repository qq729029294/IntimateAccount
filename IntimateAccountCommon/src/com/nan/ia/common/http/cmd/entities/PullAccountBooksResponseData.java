/**
 * @ClassName:     SyncDataResponse.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

import com.nan.ia.common.entities.AccountBook;

public class PullAccountBooksResponseData {
	List<AccountBook> books;

	public List<AccountBook> getBooks() {
		return books;
	}

	public void setBooks(List<AccountBook> books) {
		this.books = books;
	}
}
