package com.nan.ia.app.http.cmd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.nan.ia.app.R;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.HttpRequestHelper;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

/**
 * Created by weijiangnan on 15-5-13.
 */
public abstract class BaseHttpCmd {
	/**
	 * Created by weijiangnan on 14-8-13.
	 */
	public static class HttpCmdInfo {
	    public enum HttpMethod {
	        GET,
	        POST
	    }

	    protected String url;
	    protected HttpMethod httpMethod = HttpMethod.GET;
	    protected int scoketTimeout = 20000;
	    protected int connectTimeout = 10000;

	    public int getConnectTimeout() {
	        return connectTimeout;
	    }

	    public void setConnectTimeout(int connectTimeout) {
	        this.connectTimeout = connectTimeout;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public HttpMethod getHttpMethod() {
	        return httpMethod;
	    }

	    public void setHttpMethod(HttpMethod httpMethod) {
	        this.httpMethod = httpMethod;
	    }

	    public int getScoketTimeout() {
	        return scoketTimeout;
	    }

	    public void setScoketTimeout(int scoketTimeout) {
	        this.scoketTimeout = scoketTimeout;
	    }
	}
	
	private static class ParamNameValuePair implements NameValuePair {
		String name;
		String value;
		
		public ParamNameValuePair(String name, Object value) {
			this.name = name;
			this.value = value.toString();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}
	}
	
	protected abstract HttpCmdInfo getHttpCmdInfo();

	/**
	 * 用于检查输入值是否有效
	 * 
	 * @returnss
	 */
	public boolean checkInputValid() {
		return true;
	}

	static public interface HttpCmdCallback {
		public void onFinished(CustomHttpResponse response);
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
		
		AsyncTask<Integer, Integer, CustomHttpResponse> task = new AsyncTask<Integer, Integer, CustomHttpResponse>() {

			@Override
			protected CustomHttpResponse doInBackground(Integer... params) {
				return send(context, useCache);
			}

			@Override
			protected void onPostExecute(CustomHttpResponse result) {
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
	public CustomHttpResponse send(Context context, boolean useCache) {
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
		
		return handleResponse(context, response);
	}

    /**
     * 处理返回值
     * @param res
     */
	protected CustomHttpResponse handleResponse(Context context, CustomHttpResponse response) {
		try {
            if (response == null) {
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
	
	@SuppressLint("DefaultLocale")
	protected List<NameValuePair> getParameters() {
		Class<?> x = this.getClass();
		Field[] fields = x.getDeclaredFields();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			int modifier = field.getModifiers();

			// 非静态变量，都认为是参数
			if ((modifier & Modifier.STATIC) == 0) {
				try {
					String getMethodName = "get"
							+ name.replaceFirst(name.substring(0, 1), name
									.substring(0, 1).toUpperCase());
					Method method = this.getClass().getMethod(getMethodName);

					// 不添加为空的值
					if (field.getClass() instanceof Object
							&& method.invoke(this) == null) {
						continue;
					}

					nameValuePairs.add(new ParamNameValuePair(name, method
							.invoke(this)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return nameValuePairs;
	}
}