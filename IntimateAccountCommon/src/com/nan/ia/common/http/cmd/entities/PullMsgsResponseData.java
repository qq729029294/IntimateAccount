/**
 * @ClassName:     PullMsgsResponseData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

import com.nan.ia.common.entities.InviteMemberInfo;

public class PullMsgsResponseData {
	List<InviteMemberInfo> inviteMemberInfos;

	public List<InviteMemberInfo> getInviteMemberInfos() {
		return inviteMemberInfos;
	}

	public void setInviteMemberInfos(List<InviteMemberInfo> inviteMemberInfos) {
		this.inviteMemberInfos = inviteMemberInfos;
	}
}