/**
 * @ClassName:     BizFacade.java
 * @Description:   通用业务层 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.biz;

import java.util.Map;

import com.nan.ia.server.entities.RegistionVfCode;

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
	
	//
}
