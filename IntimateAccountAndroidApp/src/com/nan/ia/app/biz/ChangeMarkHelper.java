/**
 * @ClassName:     UpdateHelper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月14日 
 */

package com.nan.ia.app.biz;

import java.util.HashMap;
import java.util.HashSet;

public class ChangeMarkHelper {
	static HashMap<String, HashSet<String>> sChangeMarkCache = new HashMap<String, HashSet<String>>();
	
	public static void markChange(String changeType) {
		sChangeMarkCache.put(changeType, new HashSet<String>());
	}
	
	public static boolean checkChange(String changeType, String checker) {
		if (!sChangeMarkCache.containsKey(changeType)) {
			sChangeMarkCache.put(changeType, new HashSet<String>());
		} 
		
		if (sChangeMarkCache.get(changeType).contains(checker)) {
			return false;
		}
		
		sChangeMarkCache.get(changeType).add(checker);
		return true;
	}
}
