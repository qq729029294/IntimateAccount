/**
 * @ClassName:     SyncDataServerCmd.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.app.http.cmd.server;

import com.nan.ia.app.constant.Url;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.common.http.cmd.entities.InviteMemberRequestData;
import com.nan.ia.common.http.cmd.entities.NullResponseData;

public class InviteMemberServerCmd extends BaseServerCmd<InviteMemberRequestData, NullResponseData> {

	@Override
	protected HttpCmdInfo createHttpCmdInfo() {
		HttpCmdInfo httpCmdInfo = new HttpCmdInfo();
		httpCmdInfo.setHttpMethod(HttpMethod.POST);
		httpCmdInfo.setUrl(Url.URL_INVITE_MEMBER);
		
		return httpCmdInfo;
	}
}