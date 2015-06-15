/**
 * @ClassName:     VerifyMailVfCodeServerCmd.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.app.http.cmd.server;

import com.nan.ia.app.constant.Url;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.common.http.cmd.entities.NullResponseData;
import com.nan.ia.common.http.cmd.entities.VerifyVfCodeRequestData;

public class VerifyVfCodeServerCmd extends
		BaseServerCmd<VerifyVfCodeRequestData, NullResponseData> {
	
	@Override
	protected HttpCmdInfo createHttpCmdInfo() {
		HttpCmdInfo httpCmdInfo = new HttpCmdInfo();
		httpCmdInfo.setHttpMethod(HttpMethod.POST);
		httpCmdInfo.setUrl(Url.URL_VERIFY_VF_CODE);
		
		return httpCmdInfo;
	}
}
