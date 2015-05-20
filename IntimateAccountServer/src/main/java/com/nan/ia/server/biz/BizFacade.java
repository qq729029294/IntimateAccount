/**
 * @ClassName:     BizFacade.java
 * @Description:   通用业务层 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.biz;

import java.util.Map;

import com.nan.ia.server.entities.RegistionVfCode;
import com.nan.ia.server.utils.SendMail;

public class BizFacade {
	Map<String, RegistionVfCode> mapVfCode;
	
	private static BizFacade sInstance = null;
	public static BizFacade getInstance() {
		if (null != sInstance) {
			return sInstance;
		}
		
		synchronized (BizFacade.class) {
			if (null == sInstance) {
				sInstance = new BizFacade();
			}
		}
		
		return sInstance;
	}
	
	// 添加注册码
	public void pushCacheVfCode(String username, int vfCode) {
//		RegistionVfCode code = new RegistionVfCode();
//		code.setUsername(username);
//		
//		mapVfCode
	}
	
	public void sendVerificationCodeByEmail(String mailAddress) {
        SendMail sendMail = new SendMail();
        sendMail.setSmtpServer("smtp.126.com");
        //此处设置登录的用户名
        sendMail.setUsername("weijiangnan@126.com");
        //此处设置登录的密码
        sendMail.setPassword("lj86598095");
        //设置收件人的地址
        sendMail.setTo("729029294@qq.com");
        //设置发送人地址
        sendMail.setFrom("weijiangnan@126.com");
        //设置标题
        sendMail.setSubject("测试邮件标题！");
        //设置内容
        sendMail.setContent("你好这是一个带多附件的测试邮件！"); 
        //粘贴附件
        //sendMail.attachfile("C:/Login6 (1).jpg");
        if (sendMail.send())
        {
            System.out.println("---邮件发送成功---");
        }
	}

}
