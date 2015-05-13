/**
 * @ClassName:     HttpResult.java
 * @Description:   HTTP请求结果对象 
 * 
 * @author         weijiangnan create on 2015年5月14日 
 */

package com.nan.ia.app.http.cmd;

public class HttpResult {
	public static final int RET_SUCCESS = 0;
	public static final int RET_HTTP_ERROR = -1;    		// HTTP请求错误
	public static final int RET_SERVER_RESUT_ERROR = -2;	// 服务器返回异常
	
	int ret = RET_HTTP_ERROR;
	String errMsg = "";
	String response = "";
	
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
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
}
