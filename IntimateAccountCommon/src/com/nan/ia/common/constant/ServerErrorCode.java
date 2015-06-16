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
	public static final int RET_ASSCESS_DB_ERROR 		= -5;       // 存取数据库错误
	public static final int RET_MAIL_ALREADY_REGISTER	= -10;      // 邮箱已经被注册
	public static final int RET_SEND_MAIL_FAIL			= -11;      // 发送验证邮件失败
	public static final int RET_INVALID_VF_CODE 		= -12;      // 无效的验证码
	public static final int RET_PASSWORD_ERROR			= -20;      // 密码错误
	public static final int RET_USERNAME_NOT_EXIST 		= -21;		// 用户名不存在
	public static final int RET_HAS_BEEN_MEMBER  		= -31;		// 已经是成员
	public static final int RET_HTTP_REQUEST_EXCEPTION 	= -910;		// HTTP请求异常
	public static final int RET_HTTP_REQUEST_ERROR 		= -911;		// HTTP请求错误
	public static final int RET_SERVER_NOT_RESPONDING	= -920;		// 服务器没有响应
	public static final int RET_UNABLE_PARSE_RESPONDING	= -921;     // 无法解析服务器响应
}