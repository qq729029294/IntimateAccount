/**
 * @ClassName:     ServerErrorCode.java
 * @Description:   服务器错误码 
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app;

public class ServerErrorCode {
	public static final int RET_SUCCESS = 0;
	public static final int RET_HTTP_ERROR = -1;    		// HTTP请求错误
	public static final int RET_SERVER_NOT_RESPONDING = -2;	// 服务器没有响应
}
