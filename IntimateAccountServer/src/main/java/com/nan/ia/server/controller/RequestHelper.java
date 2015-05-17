/**
 * @ClassName:     HttpControllerHelper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月17日 
 */

package com.nan.ia.server.controller;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.EmptyRequestData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

public class RequestHelper {
	
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
			return true;
		}
		
		return false;
	}
	
	public static <responseDataClass> String responseParamError(Type responseDataClass) {
		ServerResponse<responseDataClass> response = new ServerResponse<responseDataClass>();
		response.setRet(ServerErrorCode.RET_PARAM_ERROR);
		response.setErrMsg("请求参数错误");
		
		Gson gson = new Gson();
		return gson.toJson(response);
	}
}
