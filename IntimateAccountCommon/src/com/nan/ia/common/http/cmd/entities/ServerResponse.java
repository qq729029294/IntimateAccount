/**
 * @ClassName:     ServerResponse.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月17日 
 */

package com.nan.ia.common.http.cmd.entities;

import com.nan.ia.common.constant.ServerErrorCode;

public class ServerResponse<RESPONSE_DATA> {
	
	int ret = ServerErrorCode.RET_HTTP_REQUEST_ERROR;
	String errMsg = "";
	RESPONSE_DATA data = null;
	
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public RESPONSE_DATA getData() {
		return data;
	}
	public void setData(RESPONSE_DATA data) {
		this.data = data;
	}
}
