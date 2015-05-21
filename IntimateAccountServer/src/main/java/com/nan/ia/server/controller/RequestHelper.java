/**
 * @ClassName:     HttpControllerHelper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月17日 
 */

package com.nan.ia.server.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.EmptyRequestData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;
import com.nan.ia.common.utils.BoolResult;

public class RequestHelper {
	
	/**
	 * 检查Token是否有效
	 * @param validate
	 * @return
	 */
	public static boolean checkTokenValid(String token) {
		return true;
	}
	
	public static int getUserIdFromToken(String token) {
		return Integer.valueOf(token);
	}
	
	public static <T> boolean parseRequestData(HttpServletRequest request, T requestData, Class<T> requestDataClass) {
		if (requestDataClass.equals(EmptyRequestData.class)) {
			try {
				requestData = requestDataClass.newInstance();
				return true;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		String dataJson = request.getParameter("data");
		if (null != dataJson && !dataJson.isEmpty()) {
			Gson gson = new Gson();
			requestData = gson.fromJson(dataJson, requestDataClass);
			return requestData != null;
		}
		
		return false;
	}
	
	public static <T> BoolResult<T> parseRequestData(HttpServletRequest request, Class<T> requestDataClass) {
		if (requestDataClass.equals(EmptyRequestData.class)) {
			try {
				return BoolResult.True(requestDataClass.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return BoolResult.False();
		}
		
		String dataJson = request.getParameter("data");
		if (null != dataJson && !dataJson.isEmpty()) {
			Gson gson = new Gson();
			T requestData = gson.fromJson(dataJson, requestDataClass);
			if (requestData != null) {
				return BoolResult.True(requestData);
			}
		}
		
		return BoolResult.False();
	}
	
	/**
	 * 成功
	 * @return
	 */
	public static String responseSuccess() {
		ServerResponse<Object> response = new ServerResponse<Object>();
		response.setRet(ServerErrorCode.RET_SUCCESS);
		String errMsg = "请求成功";
		response.setErrMsg(errMsg);
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	/**
	 * 成功
	 * @return
	 */
	public static <T> String responseSuccess(T responseData) {
		ServerResponse<T> response = new ServerResponse<T>();
		response.setRet(ServerErrorCode.RET_SUCCESS);
		String errMsg = "请求成功";
		response.setErrMsg(errMsg);
		response.setData(responseData);
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	/**
	 * 错误
	 * @param ret
	 * @param otherMsg
	 * @return
	 */
	public static String responseError(int ret, String msg) {
		ServerResponse<Object> response = new ServerResponse<Object>();
		response.setRet(ret);
		response.setErrMsg(msg);
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	/**
	 * 参数错误
	 * @param responseDataClass
	 * @return
	 */
	public static String responseParamError(String otherMsg) {
		ServerResponse<Object> response = new ServerResponse<Object>();
		response.setRet(ServerErrorCode.RET_PARAM_ERROR);
		String errMsg = "请求参数错误";
		if (null != otherMsg && !otherMsg.isEmpty()) {
			errMsg = errMsg + ":" + otherMsg;
		}
		response.setErrMsg(errMsg);
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
	/**
	 * 修改数据库错误
	 * @param responseDataClass
	 * @return
	 */
	public static String responseAccessDBError(String otherMsg) {
		ServerResponse<Object> response = new ServerResponse<Object>();
		response.setRet(ServerErrorCode.RET_ASSCESS_DB_ERROR);
		String errMsg = "存取数据库错误";
		if (null != otherMsg && !otherMsg.isEmpty()) {
			errMsg = errMsg + ":" + otherMsg;
		}
		response.setErrMsg(errMsg);
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
}
