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

	/**
	 * 标记变更
	 * @param changeType
	 */
	public static void markChange(String changeType) {
		sChangeMarkCache.put(changeType, new HashSet<String>());
	}
	
	/**
	 * 取消标记
	 * @param changeType
	 */
	public static void cleanMark(String changeType) {
		sChangeMarkCache.remove(changeType);
	}
	
	/**
	 * 检查指定的checker是否有变更
	 * @param changeType
	 * @param checker
	 * @return
	 */
	public static boolean checkChange(String changeType, String checker) {
		if (!sChangeMarkCache.containsKey(changeType)) {
			return false;
		} 
		
		if (sChangeMarkCache.get(changeType).contains(checker)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 检查并应用变更
	 * @param changeType
	 * @param checker
	 * @return
	 */
	public static boolean applyChange(String changeType, String checker) {
		if (!checkChange(changeType, checker)) {
			return false;
		}
		
		sChangeMarkCache.get(changeType).add(checker);
		return true;
	}
}
