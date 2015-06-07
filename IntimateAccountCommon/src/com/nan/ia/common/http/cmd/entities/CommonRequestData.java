/**
 * @ClassName:     BaseRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.common.http.cmd.entities;

public class CommonRequestData {
	int userId;
	String token;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
