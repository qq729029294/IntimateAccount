/**
 * @ClassName:     BaseServerCmd.java
 * @Description:   服务器指令基类
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app.http.cmd.server;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import com.nan.ia.app.R;
import com.nan.ia.app.ServerErrorCode;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.cmd.BaseHttpCmd;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.MainThreadExecutor;
import com.nan.ia.app.widget.CustomToast;

public abstract class BaseServerCmd extends BaseHttpCmd {
	public static class ServerResult {
		
		int ret = ServerErrorCode.RET_HTTP_REQUEST_ERROR;
		String errMsg = "";
		String response = "";
		
		public int getRet() {
			return ret;
		}
		public void setRet(int ret) {
			this.ret = ret;
		}
		public String getErrMsg() {
			return errMsg;
		}
		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
	}
	static public interface ServerCmdCallback {
		public void onFinished(ServerResult result);
	}

	/**
	 * 异步请求
	 * @param context
	 * @param useCache
	 * @param callback
	 */
	public void sendToServerAsync(final Context context, final boolean useCache, final ServerCmdCallback callback) {
		if (!Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
			// 必须在主线程调用
			throw new AssertionFailedError("BaseServerCmd.sendToServerAsync must be called on main thread.");
		}
		
		AsyncTask<Integer, Integer, ServerResult> task = new AsyncTask<Integer, Integer, ServerResult>() {

			@Override
			protected ServerResult doInBackground(Integer... params) {
				return sendToServer(context, useCache);
			}

			@Override
			protected void onPostExecute(ServerResult result) {
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
	public ServerResult sendToServer(Context context, boolean useCache) {
		return this.handleServerResponse(context, super.send(context, useCache));
	}
	
    /**
     * 处理返回值
     * @param res
     */
	protected ServerResult handleServerResponse(Context context, CustomHttpResponse response) {
		final ServerResult serverResult = new ServerResult();
		try {
			if (response == null) {
				response = new CustomHttpResponse();
			}
			
            if (response.getStatusCode() == CustomHttpResponse.HTTP_REQUEST_EXCEPTION) {
            	// 请求异常
            	serverResult.setRet(ServerErrorCode.RET_HTTP_REQUEST_EXCEPTION);
            	serverResult.setErrMsg(context.getString(R.string.http_request_exception));
            	return serverResult;
            } else if (response.getStatusCode() != HttpStatus.SC_OK) {
            	// 请求错误
            	serverResult.setRet(ServerErrorCode.RET_HTTP_REQUEST_ERROR);
            	serverResult.setErrMsg(context.getString(R.string.fmt_http_request_error) + response.getStatusCode());
            	return serverResult;
            }
            
        	// HTTP请求成功
        	if (null == response.getResponse() || response.getResponse().isEmpty()) {
				// 服务器没有响应
        		serverResult.setRet(ServerErrorCode.RET_SERVER_NOT_RESPONDING);
        		serverResult.setErrMsg(context.getString(R.string.server_not_responding));
        		return serverResult;
			}
			
			JSONObject jsonObject = new JSONObject(response.getResponse());
            // 设置返回码
			serverResult.setRet(jsonObject.optInt("ret", ServerErrorCode.RET_UNABLE_PARSE_RESPONDING));
			if (serverResult.getRet() == ServerErrorCode.RET_UNABLE_PARSE_RESPONDING) {
				// 无法解析的服务器响应
				serverResult.setErrMsg(context.getString(R.string.unable_parse_responding));
				return serverResult;
			}
			
			// 设置错误信息
			serverResult.setErrMsg(jsonObject.optString("msg", ""));
			
			if (serverResult.getRet() == ServerErrorCode.RET_SUCCESS) {
				// 成功
				serverResult.setResponse(jsonObject.optString("data", ""));
			} else {
				if (serverResult.getErrMsg().isEmpty()) {
					serverResult.setErrMsg(context.getString(R.string.fmt_server_error));
				}
				
				// 弹出错误信息
				MainThreadExecutor.run(new Runnable() {
					
					@Override
					public void run() {
						CustomToast.showToast(serverResult.getErrMsg());
					}
				});
			}
		} catch (Exception e) {
			LogUtils.e("handleServerResponse exception", e);
		}
		
		return serverResult;
	}
}
