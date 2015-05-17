package com.nan.ia.app.http.cmd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.google.gson.Gson;
import com.nan.ia.app.R;
import com.nan.ia.app.http.CustomHttpResponse;
import com.nan.ia.app.http.HttpRequestHelper;
import com.nan.ia.app.http.cmd.BaseHttpCmd.HttpCmdInfo.HttpMethod;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.app.utils.MainThreadExecutor;
import com.nan.ia.app.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

/**
 * Created by weijiangnan on 15-5-13.
 */
public abstract class BaseHttpCmd<T> {
	
	protected HttpCmdInfo mHttpCmdInfo = null;
	
	protected abstract HttpCmdInfo createHttpCmdInfo();

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
	public void sendAsync(final Context context, final T input, final boolean useCache, final HttpCmdCallback callback) {
		if (!Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
			// 必须在主线程调用
			throw new AssertionFailedError("BaseHttpCmd.sendAsync must be called on main thread.");
		}
		
		AsyncTask<Integer, Integer, CustomHttpResponse> task = new AsyncTask<Integer, Integer, CustomHttpResponse>() {

			@Override
			protected CustomHttpResponse doInBackground(Integer... params) {
				return send(context, input, useCache);
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
	public CustomHttpResponse send(Context context, T input, boolean useCache) {
		if (null == mHttpCmdInfo) {
			// 构建HTTP请求信息
			mHttpCmdInfo = createHttpCmdInfo();
		}
		
		List<NameValuePair> parameters = getParameters(input);
		NameValuePair[] parameterArray = parameters.toArray(new NameValuePair[parameters.size()]);
		
		CustomHttpResponse response = null;
		
		try {
			if (mHttpCmdInfo.getHttpMethod() == HttpMethod.POST) {
				response = HttpRequestHelper.postByHttpClient(context,
						useCache, mHttpCmdInfo.getUrl(), parameterArray);
			} else {
				response = HttpRequestHelper.getByHttpClient(context,
						useCache, mHttpCmdInfo.getUrl(), parameterArray);
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
	protected CustomHttpResponse handleResponse(final Context context, CustomHttpResponse response) {
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
				MainThreadExecutor.run(new Runnable() {

					@Override
					public void run() {
						CustomToast.showToast(R.string.http_request_exception);
					}
				});
			} else {
				// 网络错误
				final String errMsg = context.getString(R.string.fmt_http_request_error) + response.getStatusCode();
				MainThreadExecutor.run(new Runnable() {

					@Override
					public void run() {
						CustomToast.showToast(errMsg);
					}
				});
			}
			
		} catch (Exception e) {
			LogUtils.e("handleResponse exception", e);
		}
		
		return response;
	}
	
	@SuppressLint("DefaultLocale")
	protected List<NameValuePair> getParameters(T input) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (null == input) {
			// 空
			return nameValuePairs;
		}
		
		Class<?> x = input.getClass();
		Field[] fields = x.getDeclaredFields();

		
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
					Method method = input.getClass().getMethod(getMethodName);

					// 不添加为空的值
					if (field.getClass() instanceof Object
							&& method.invoke(input) == null) {
						continue;
					}

					nameValuePairs.add(new ParamNameValuePair(name, method
							.invoke(input)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return nameValuePairs;
	}
	

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
			
			if (value instanceof List<?>) {
				Gson gson = new Gson();
				this.value = gson.toJson(value);				
			} else {
				this.value = value.toString();
			}
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
}