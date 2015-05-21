/**
 * @ClassName:     BoolResult.java
 * @Description:   bool����ֵ�İ�װ�࣬����boolֵ��������Ϣ���ͷ��ض��� 
 * 
 * @author         weijiangnan create on 2015��5��18�� 
 */

package com.nan.ia.common.utils;

public class BoolResult<T> {
	boolean isTrue = true;
	String errMsg = "";
	T result;
	
	public static <T> BoolResult<T> False(String errMsg) {
		BoolResult<T> result = new BoolResult<T>();
		result.isTrue = false;
		result.errMsg = errMsg;
		
		return result;
	}
	
	public static <T> BoolResult<T> False() {
		BoolResult<T> result = new BoolResult<T>();
		result.isTrue = false;
		result.errMsg = "";
		
		return result;
	}
	
	public static <T> BoolResult<T> True(T result) {
		BoolResult<T> funcResult = new BoolResult<T>();
		funcResult.isTrue = true;
		funcResult.errMsg = "";
		funcResult.result = result;
		
		return funcResult;
	}
	
	public static <T> BoolResult<T> True() {
		BoolResult<T> funcResult = new BoolResult<T>();
		funcResult.isTrue = true;
		funcResult.errMsg = "";
		
		return funcResult;
	}
	
	public boolean isTrue() {
		return isTrue;
	}
	
	public boolean isFalse() {
		return !isTrue;
	}
	
	public String errMsg() {
		return errMsg;
	}

	public T result() {
		return result;
	}
}
