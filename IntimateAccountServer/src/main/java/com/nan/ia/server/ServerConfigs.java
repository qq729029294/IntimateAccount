package com.nan.ia.server;

/**
 * @ClassName:     ServerConfigs.java
 * @Description:   配置
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

public class ServerConfigs {
	// 验证码过期时间，30分钟
	public static final long VERIFICATION_CODE_EXPIRED_TIME = 60 * 30 * 1000; 
	// 邮件相关
	public static final String MAIL_SMTP_SERVER = "smtp.126.com";
	public static final String MAIL_FROM_MAIL = "weijiangnan@126.com";
	public static final String MAIL_FROM_MAIL_PASSWORD = "lj86598095";
	public static final String MAIL_SUBJECT = "亲密账-账号注册";
	public static final String MAIL_KEY_VERIFICATION_CODE = "${verification_code}";
	public static final String MAIL_FMT_CONTENT = "欢迎加入亲密帐：\n\n请输入以下验证码，完成注册\n${verification_code}\n\n亲密账";
}