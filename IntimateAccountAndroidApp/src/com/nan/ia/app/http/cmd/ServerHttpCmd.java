/**
 * @ClassName:     ServerHttpCmd.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月17日 
 */

package com.nan.ia.app.http.cmd;

public class ServerHttpCmd extends BaseHttpCmd<ServerHttpInput> {
	public ServerHttpCmd(HttpCmdInfo httpCmdInfo) {
		mHttpCmdInfo = httpCmdInfo;
	}

	@Override
	protected HttpCmdInfo createHttpCmdInfo() {
		return mHttpCmdInfo;
	}
}