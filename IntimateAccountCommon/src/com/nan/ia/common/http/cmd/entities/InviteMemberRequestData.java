/**
 * @ClassName:     InviteBookMemberRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月16日 
 */

package com.nan.ia.common.http.cmd.entities;

public class InviteMemberRequestData extends CommonRequestData {
	String inviteeUsername;
	int accountBookId;
	String accountBookName;
	public String getInviteeUsername() {
		return inviteeUsername;
	}
	public void setInviteeUsername(String inviteeUsername) {
		this.inviteeUsername = inviteeUsername;
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