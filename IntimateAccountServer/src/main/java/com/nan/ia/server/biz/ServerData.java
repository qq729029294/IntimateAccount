/**
 * @ClassName:     ServerData.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.server.biz;

import java.util.Map;

import com.nan.ia.server.entities.RegistionVfCode;

public class ServerData {
	Map<String, RegistionVfCode> mapVfCode;

	public Map<String, RegistionVfCode> getMapVfCode() {
		return mapVfCode;
	}

	public void setMapVfCode(Map<String, RegistionVfCode> mapVfCode) {
		this.mapVfCode = mapVfCode;
	}
}
