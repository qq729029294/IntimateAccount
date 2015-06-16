/**
 * @ClassName:     PullUserInfosRequestData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月16日 
 */

package com.nan.ia.common.http.cmd.entities;

import java.util.List;

public class PullUserInfosRequestData extends CommonRequestData {
	List<Integer> userIds;

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
}
