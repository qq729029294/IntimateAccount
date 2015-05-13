package com.nan.ia.app.http.cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.nan.ia.app.R;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.HttpRequestHelper;
import com.nan.ia.app.http.cmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.app.utils.LogUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

/**
 * Created by weijiangnan on 15-5-13.
 */
public abstract class BaseHttpCmd {
	protected abstract HttpCmdInfo getHttpCmdInfo();

	/**
	 * 用于检查输入值是否有效
	 * 
	 * @return
	 */
	public boolean checkInputValid() {
		return true;
	}

	static public interface HttpCmdCallback {
		public void onFinished(HttpResult result);
	}

	/**
	 * 异步请求
	 * @param context
	 * @param useCache
	 * @param callback
	 */
	public void sendAsync(final Context context, final boolean useCache, final HttpCmdCallback callback) {
		if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
			// 必须在主线程调用
			throw new AssertionFailedError("BaseHttpCmd.sendAsync must be called at main thread.");
		}
		
		AsyncTask<Integer, Integer, HttpResult> task = new AsyncTask<Integer, Integer, HttpResult>() {

			@Override
			protected HttpResult doInBackground(Integer... params) {
				return send(context, useCache);
			}

			@Override
			protected void onPostExecute(HttpResult result) {
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
	public HttpResult send(Context context, boolean useCache) {
		HttpCmdInfo httpCmdInfo = getHttpCmdInfo(); // 获取请求的信息
		List<NameValuePair> parameters = getParameters();
		NameValuePair[] parameterArray = parameters.toArray(new NameValuePair[parameters.size()]);
		
		CustomHttpResponse response = null;
		
		try {
			if (httpCmdInfo.getHttpMethod() == HttpMethod.POST) {
				response = HttpRequestHelper.postByHttpClient(context,
						useCache, httpCmdInfo.getUrl(), parameterArray);
			} else {
				response = HttpRequestHelper.getByHttpClient(context,
						useCache, httpCmdInfo.getUrl(), parameterArray);
			}
		} catch (Exception e) {
			response = new CustomHttpResponse();
			LogUtils.w("http request error.", e);
		}
		
		return handleResponse(response);
	}
	
	protected List<NameValuePair> getParameters() {
		return new ArrayList<NameValuePair>();
	}


    /**
     * 处理返回值
     * @param res
     */
	protected HttpResult handleResponse(Context context, CustomHttpResponse response) {
		try {
			HttpResult result = new HttpResult();
            if (response == null) {
                // 请求异常，直接返回
                result.setRet(HttpResult.RET_HTTP_ERROR);
                result.setErrMsg(context.getString(R.string.http_request_exception));
            }

			if (response.getStatusCode() == HttpStatus.SC_OK) {
				// 读取返回的Json
				InputStream is;
				is = res.getEntity().getContent();

				StringBuffer buffer = new StringBuffer();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(is));
				String str;
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}

				mResult.setRet(AuroraSrvErrorCode.ERROR);

                String json = buffer.toString();
                AuroraLog.d(json);
				JSONObject jsonObject = new JSONObject(json);
                // 设置返回码
                mResult.setRet(jsonObject.optInt("ret", AuroraSrvErrorCode.ERROR));
                // 转换数据
                JSONObject dataJsonObject = jsonObject.optJSONObject("data");
                if (null != dataJsonObject)
				parseResult(dataJsonObject);
			} else {
				AuroraLog.e("Http request error, httpStatus=" + mResult.getHttpStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void parseResult(JSONObject resultObject) {
		mResult.parseGeneralResult(resultObject);
	}
}