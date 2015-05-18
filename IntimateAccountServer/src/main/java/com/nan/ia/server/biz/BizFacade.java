/**
 * @ClassName:     BizFacade.java
 * @Description:   通用业务层 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.biz;

public class BizFacade {
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
}
