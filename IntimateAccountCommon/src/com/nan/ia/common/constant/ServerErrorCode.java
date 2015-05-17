/**
 * @ClassName:     ServerErrorCode.java
 * @Description:   服务器错误码 
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.common.constant;

public class ServerErrorCode {
	public static final int RET_SUCCESS 				= 0;
	public static final int RET_PARAM_ERROR 			= -4;       // 参数错误
	
	public static final int RET_HTTP_REQUEST_EXCEPTION 	= -10;		// HTTP请求错误
	public static final int RET_HTTP_REQUEST_ERROR 		= -11;		// HTTP请求错误
	public static final int RET_SERVER_NOT_RESPONDING	= -20;		// 服务器没有响应
	public static final int RET_UNABLE_PARSE_RESPONDING	= -21;      // 无法解析服务器响应
}