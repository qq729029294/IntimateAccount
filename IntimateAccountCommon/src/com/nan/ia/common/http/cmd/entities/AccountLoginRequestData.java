/**
 * @ClassName:     LoginRequestData.java
 * @Description:   登录
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.common.http.cmd.entities;

public class AccountLoginRequestData extends BaseRequestData {
	String username;
	String password;
	int accountType;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
}
