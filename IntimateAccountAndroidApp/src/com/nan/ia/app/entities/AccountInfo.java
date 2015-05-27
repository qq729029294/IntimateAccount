/**
 * @ClassName:     UserInfo.java
 * @Description:   用户信息 
 * 
 * @author         weijiangnan create on 2015-5-15
 */

package com.nan.ia.app.entities;

public class AccountInfo {
	private int accountType;
	private String username;
	private int userId;
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}