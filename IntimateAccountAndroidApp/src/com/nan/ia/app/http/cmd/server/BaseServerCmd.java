/**
 * @ClassName:     BaseServerCmd.java
 * @Description:   服务器指令基类
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app.http.cmd.server;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.ResponseDate;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import com.google.gson.Gson;
import com.nan.ia.app.R;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.cmd.BaseHttpCmd;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo;
import com.nan.ia.app.http.cmd.ServerHttpCmd;
import com.nan.ia.app.http.cmd.ServerHttpInput;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.MainThreadExecutor;
import com.nan.ia.app.widget.CustomToast;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

public abstract class BaseServerCmd<REQUEST_DATA, RESPONSE_DATA> {
	
	protected ServerHttpCmd mHttpCmd = null;
	protected Gson mGson = new Gson();
	
	protected abstract HttpCmdInfo createHttpCmdInfo();
	
	/**
	 * 异步请求
	 * @param context
	 * @param useCache
	 * @param callback
	 */
	public void sendAsync(final Context context, final REQUEST_DATA requestData, final Class<RESPONSE_DATA> typeOfResponse, final boolean useCache, final ServerCmdCallback<RESPONSE_DATA> callback) {
		if (!Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
			// 必须在主线程调用
			throw new AssertionFailedError("BaseServerCmd.sendToServerAsync must be called on main thread.");
		}
		
		AsyncTask<Integer, Integer, ServerResponse<RESPONSE_DATA>> task = new AsyncTask<Integer, Integer, ServerResponse<RESPONSE_DATA>>() {

			@Override
			protected ServerResponse<RESPONSE_DATA> doInBackground(Integer... params) {
				return send(context, requestData, typeOfResponse, useCache);
			}

			@Override
			protected void onPostExecute(ServerResponse<RESPONSE_DATA> result) {
				if (null != callback) {
					callback.onFinished(result);
				}
				
				super.onPostExecute(result);
			}
		};
		
		task.execute(0);
	}
	
	/**
	 * 同步请求
	 * @param context
	 * @param useCache
	 * @return
	 */
	public ServerResponse<RESPONSE_DATA> send(Context context, REQUEST_DATA requestData, Class<RESPONSE_DATA> typeOfResponse, boolean useCache) {
		if (mHttpCmd == null) {
			mHttpCmd = new ServerHttpCmd(this.createHttpCmdInfo());
		}
		
		ServerHttpInput input = new ServerHttpInput();
		String dataJson = mGson.toJson(requestData);
		input.setData(dataJson);
		
		CustomHttpResponse httpResponse = mHttpCmd.send(context, input, useCache);
		ServerResponse<RESPONSE_DATA> response = this.handleServerResponse(context, httpResponse, typeOfResponse);
		return response;
	}
	
	/**
	 * 处理返回值
	 * @param context
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ServerResponse<RESPONSE_DATA> handleServerResponse(Context context, CustomHttpResponse response, Class<RESPONSE_DATA> typeOfResponse) {
		final ServerResponse<RESPONSE_DATA> serverResponse = new ServerResponse<RESPONSE_DATA>();
		try {
			
			if (response == null) {
				response = new CustomHttpResponse();
			}
			
            if (response.getStatusCode() == CustomHttpResponse.HTTP_REQUEST_EXCEPTION) {
            	// 请求异常
            	serverResponse.setRet(ServerErrorCode.RET_HTTP_REQUEST_EXCEPTION);
            	serverResponse.setErrMsg(context.getString(R.string.http_request_exception));
            	return serverResponse;
            } else if (response.getStatusCode() != HttpStatus.SC_OK) {
            	// 请求错误
            	serverResponse.setRet(ServerErrorCode.RET_HTTP_REQUEST_ERROR);
            	serverResponse.setErrMsg(context.getString(R.string.fmt_http_request_error) + response.getStatusCode());
            	return serverResponse;
            }
            
        	// HTTP请求成功
        	if (null == response.getResponse() || response.getResponse().isEmpty()) {
				// 服务器没有响应
        		serverResponse.setRet(ServerErrorCode.RET_SERVER_NOT_RESPONDING);
        		serverResponse.setErrMsg(context.getString(R.string.server_not_responding));
        		return serverResponse;
			}
			
			JSONObject jsonObject = new JSONObject(response.getResponse());
            // 设置返回码
			serverResponse.setRet(jsonObject.optInt("ret", ServerErrorCode.RET_UNABLE_PARSE_RESPONDING));
			if (serverResponse.getRet() == ServerErrorCode.RET_UNABLE_PARSE_RESPONDING) {
				// 无法解析的服务器响应
				serverResponse.setErrMsg(context.getString(R.string.unable_parse_responding));
				return serverResponse;
			}
			
			// 设置错误信息
			serverResponse.setErrMsg(jsonObject.optString("msg", ""));
			
			if (serverResponse.getRet() == ServerErrorCode.RET_SUCCESS) {
				// 成功，转换返回数据
				String dataJson = jsonObject.optString("data", "");
				RESPONSE_DATA data = mGson.fromJson(dataJson, typeOfResponse);
				serverResponse.setData(data);
			} else {
				if (serverResponse.getErrMsg().isEmpty()) {
					serverResponse.setErrMsg(context.getString(R.string.fmt_server_error));
				}
				
				// 弹出错误信息
				MainThreadExecutor.run(new Runnable() {
					
					@Override
					public void run() {
						CustomToast.showToast(serverResponse.getErrMsg());
					}
				});
			}
		} catch (Exception e) {
			LogUtils.e("handleServerResponse exception", e);
		}
		
		return serverResponse;
	}
	
	static public interface ServerCmdCallback<RESPONSE_DATA> {
		public void onFinished(ServerResponse<RESPONSE_DATA> response);
	}
}
