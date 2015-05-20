/**
 * @ClassName:     RegistionVfCode.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.server.entities;

public class RegistionVfCode {
	String username;
	int vfCode;
	long updateTime;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getVfCode() {
		return vfCode;
	}
	public void setVfCode(int vfCode) {
		this.vfCode = vfCode;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
