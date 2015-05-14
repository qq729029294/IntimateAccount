/**
 * @ClassName:     BaseServerCmd.java
 * @Description:   TODO 
 * 
 * @author         weijiangnan create on 2015-5-14
 */

package com.nan.ia.app.http.cmd.server;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import com.nan.ia.app.R;
import com.nan.ia.app.ServerErrorCode;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.HttpRequestHelper;
import com.nan.ia.app.http.cmd.BaseHttpCmd;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdCallback;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.widget.CustomToast;

public abstract class BaseServerCmd extends BaseHttpCmd {
	public static class ServerResult {
		
		int ret = ServerErrorCode.RET_HTTP_ERROR;
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
		try {
            if (response == null || response.getStatusCode() != HttpStatus.SC_OK) {
            	// 请求异常
            	response = new CustomHttpResponse();
            }
            
			if (null != response && response.getStatusCode() == HttpStatus.SC_OK) {
				// 请求成功
				return response;
			}

			if (response.getStatusCode() == CustomHttpResponse.HTTP_REQUEST_EXCEPTION) {
				// 请求异常，直接返回
            	CustomToast.showToast(R.string.http_request_exception);
			} else {
				// 网络错误
				CustomToast.showToast(context.getString(R.string.fmt_http_request_error) + response.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
