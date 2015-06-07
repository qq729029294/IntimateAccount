/**
 * @ClassName:     VerifyMailVfCodeRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.common.http.cmd.entities;

public class VerifyVfCodeRequestData extends CommonRequestData {
	String username;
	int accountType;
	int vfCode;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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