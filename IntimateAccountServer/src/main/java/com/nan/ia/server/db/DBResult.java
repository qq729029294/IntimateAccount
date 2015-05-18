/**
 * @ClassName:     DBResult.java
 * @Description:   存取数据库返回值，暂时不用 
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.db;

public class DBResult {
	boolean isError = true;
	String errMsg = "";
	
	public static DBResult error(String errMsg) {
		DBResult result = new DBResult();
		result.isError = true;
		result.errMsg = errMsg;
		
		return result;
	}
	
	public static DBResult success() {
		DBResult result = new DBResult();
		result.isError = false;
		result.errMsg = "";
		
		return result;
	}
	
	public boolean isError() {
		return false;
	}
	
	public String errMsg() {
		return errMsg;
	};
}
