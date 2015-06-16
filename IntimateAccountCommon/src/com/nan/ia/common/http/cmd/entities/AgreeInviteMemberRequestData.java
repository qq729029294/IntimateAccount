/**
 * @ClassName:     InviteBookMemberRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月16日 
 */

package com.nan.ia.common.http.cmd.entities;

public class AgreeInviteMemberRequestData extends CommonRequestData {
	int accountBookId;

	public int getAccountBookId() {
		return accountBookId;
	}

	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}
}