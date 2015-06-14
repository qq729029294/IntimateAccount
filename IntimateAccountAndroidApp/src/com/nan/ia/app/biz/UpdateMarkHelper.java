/**
 * @ClassName:     UpdateHelper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月14日 
 */

package com.nan.ia.app.biz;

import java.util.HashMap;
import java.util.HashSet;

public class UpdateMarkHelper {
	static HashMap<String, HashSet<String>> updateMarkCache = new HashMap<String, HashSet<String>>();
	
	public static final String UPDATE_TYE_RECORD = "UPDATE_TYE_RECORD";
	public static final String UPDATE_TYE_ACCOUNT_BOOK = "UPDATE_TYE_ACCOUNT_BOOK";
	
	public static void markUpdate(String updateType) {
		updateMarkCache.put(updateType, new HashSet<String>());
	}
	
	public static boolean checkNeedUpdate(String updateType, String updater) {
		if (!updateMarkCache.containsKey(updateType)) {
			updateMarkCache.put(updateType, new HashSet<String>());
		} 
		
		if (updateMarkCache.get(updateType).contains(updater)) {
			return false;
		}
		
		updateMarkCache.get(updateType).add(updater);
		return true;
	}
}
