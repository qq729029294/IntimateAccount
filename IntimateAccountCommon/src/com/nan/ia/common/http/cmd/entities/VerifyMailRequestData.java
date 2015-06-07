/**
 * @ClassName:     VerifyMailRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.common.http.cmd.entities;

public class VerifyMailRequestData extends CommonRequestData {
	String mail;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
