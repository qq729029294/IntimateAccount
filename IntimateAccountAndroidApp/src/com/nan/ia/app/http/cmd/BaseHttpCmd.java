package com.nan.ia.app.http.cmd;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by weijiangnan on 15-5-13.
 */
public abstract class BaseHttpCmd {
	protected HttpResult mResult;

	protected abstract HttpCmdInfo injectionHttpCmdInfo();

	/**
	 * 用于检查输入值是否有效
	 * 
	 * @return
	 */
	public boolean checkInputValid() {
		return true;
	}

	static public interface HttpCmdCallback {
		public void onFinished(int statusCode, );
	}

	/**
	 * 异步请求
	 *
	 * @param callback
	 */
	public void sendAsync(final HttpCmdCallback callback) {
		AuroraHttpThreadPool pool = AuroraHttpThreadPool.getInstance();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				send();
				if (null != callback) {
					callback.onFinished(mResult);
				}
			}
		});
	}

	/**
	 * 发送请求
	 *
	 */
	public void send() {
        AuroraLog.d(this.getClass().getName() + " send.");

		if (!checkInputValid()) {
			throw new RuntimeException("Input invalid.");
		}
		;

		if (null == mHttpCmdInfo) {
			mHttpCmdInfo = injectionHttpCmdInfo();
			if (null == mHttpCmdInfo) {
				throw new RuntimeException("HttpCmdInfo is no injection.");
			}
		}

		mHttpRequest = new AuroraHttpRequest();
		HttpResponse res;
		if (mHttpCmdInfo.getHttpMethod() == HttpCmdInfo.HttpMethod.GET) {
			// Get
			res = mHttpRequest.get(mHttpCmdInfo.getUrl(),
					mInput.getParameters(),
					mHttpCmdInfo.getScoketTimeout(),
					mHttpCmdInfo.getConnectTimeout());
		} else {
			// Post
			res = mHttpRequest.post(mHttpCmdInfo.getUrl(),
					mInput.getParameters(),
					mHttpCmdInfo.getScoketTimeout(),
					mHttpCmdInfo.getConnectTimeout());
		}

		// 处理返回值
		handleResponse(res);
	}

    /**
     * 处理返回值
     * @param res
     */
	protected void handleResponse(HttpResponse res) {
		try {
            if (res == null) {
                // 请求异常，直接返回
                mResult.setHttpStatus(0);
                mResult.setRet(AuroraSrvErrorCode.ERROR);
            }

			mResult.setHttpStatus(res.getStatusLine().getStatusCode());

			if (mResult.getHttpStatus() == 200) {
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