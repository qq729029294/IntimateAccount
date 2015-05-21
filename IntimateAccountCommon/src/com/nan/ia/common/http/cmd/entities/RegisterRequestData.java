/**
 * @ClassName:     RegisterRequestData.java
 * @Description:   注册 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.common.http.cmd.entities;

public class RegisterRequestData {
	String username;
	String password;
	
	int accountType;
	int vfCode;

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

	public int getVfCode() {
		return vfCode;
	}

	public void setVfCode(int vfCode) {
		this.vfCode = vfCode;
	}
}