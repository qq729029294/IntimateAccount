/**
 * @ClassName:     InviteMemberInfo.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月16日 
 */

package com.nan.ia.common.entities;

import java.io.Serializable;

public class InviteMemberInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	int inviterUserId;
	int inviteeUserId;
	int accountBookId;
	String accountBookName;
	public int getInviterUserId() {
		return inviterUserId;
	}
	public void setInviterUserId(int inviterUserId) {
		this.inviterUserId = inviterUserId;
	}
	public int getInviteeUserId() {
		return inviteeUserId;
	}
	public void setInviteeUserId(int inviteeUserId) {
		this.inviteeUserId = inviteeUserId;
	}
	public int getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
	public String getAccountBookName() {
		return accountBookName;
	}
	public void setAccountBookName(String accountBookName) {
		this.accountBookName = accountBookName;
	}
}
