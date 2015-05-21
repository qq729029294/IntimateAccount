/**
 * @ClassName:     BizFacade.java
 * @Description:   通用业务层 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.biz;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.ServerConfigs;
import com.nan.ia.server.controller.HomeController;
import com.nan.ia.server.entities.RegistionVfCode;
import com.nan.ia.server.utils.SendMail;

public class BizFacade {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	Map<String, RegistionVfCode> mMapVfCodeCache = new HashMap<String, RegistionVfCode>();
	
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
	
	/**
	 * 发送邮件验证码
	 * @param mail
	 */
	public boolean sendVfCodeByMail(String mail) {
		Random random = new Random();
        int vfCode = random.nextInt(9999);
        
        SendMail sendMail = new SendMail();
        sendMail.setSmtpServer(ServerConfigs.MAIL_SMTP_SERVER);
        sendMail.setUsername(ServerConfigs.MAIL_FROM_MAIL);
        sendMail.setPassword(ServerConfigs.MAIL_FROM_MAIL_PASSWORD);
        sendMail.setTo(mail);
        sendMail.setFrom(ServerConfigs.MAIL_FROM_MAIL);
        sendMail.setSubject(ServerConfigs.MAIL_SUBJECT);
        
        String content = ServerConfigs.MAIL_FMT_CONTENT.replace(ServerConfigs.MAIL_KEY_VERIFICATION_CODE,
        		String.valueOf(vfCode));
        sendMail.setContent(content);
        if (sendMail.send())
        {
        	logger.debug("邮件发送成功，邮件地址：" + mail);
        	
        	// 发送成功，添加验证码信息到内存中
    		RegistionVfCode registionVfCode = new RegistionVfCode();
    		registionVfCode.setUsername(mail);
    		registionVfCode.setVfCode(vfCode);
    		registionVfCode.setUpdateTime(System.currentTimeMillis());
    		
        	synchronized (mMapVfCodeCache) {
				mMapVfCodeCache.put(mail, registionVfCode);
			}
        	
        	return true;
        } else {
        	logger.error("邮件发送失败，邮件地址：" + mail);
        	
        	return false;
        }
	}
	
	/**
	 * 验证验证码是否有效
	 * @param username
	 * @param vfCode
	 * @return
	 */
	public BoolResult<Object> verificationCode(String username, int vfCode) {
		if (!mMapVfCodeCache.containsKey(username) || null == mMapVfCodeCache.get(username)) {
			return BoolResult.False("无效的验证码");
		}
		
		RegistionVfCode registionVfCode = mMapVfCodeCache.get(username);
		if (Integer.valueOf(registionVfCode.getVfCode()) == vfCode) {
			if (System.currentTimeMillis() - registionVfCode.getUpdateTime()
					< ServerConfigs.VERIFICATION_CODE_EXPIRED_TIME) {
				return BoolResult.True();
			} else {
				return BoolResult.False("验证码已过期，请重新发送");
			}
		} else {
			return BoolResult.False("无效的验证码");
		}
	}
}
