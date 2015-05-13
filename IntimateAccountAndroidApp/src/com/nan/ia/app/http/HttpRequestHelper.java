package com.nan.ia.app.http;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import cn.eoe.app.db.DBHelper;
import cn.eoe.app.db.RequestCacheColumn;

import android.content.Context;
import android.database.Cursor;

/**
 * @author Create by weijiangnan on 2015-5-13
 */
public class HttpRequestHelper {
	/**
	 * 缓存有效时间
	 */
	private static final long CACHE_ACTIVE_DURATION_TIME = 100 * 60 * 60 * 24 * 365;
		
	// 内存缓存
	private static LinkedHashMap<String, SoftReference<String>> sRequestCache = new LinkedHashMap<String, SoftReference<String>>(
			20);
	
	
    // 网络连接部分
    public static CustomHttpResponse postByHttpClient(Context context,
    		boolean useCache, String url, NameValuePair... nameValuePairs) throws Exception {
    	CustomHttpResponse response = new CustomHttpResponse();
    	
    	// 使用缓存
    	if (useCache) {
    		// 首先查找内存
    		response.setResponse(getResponseFromGlobal(url));
    		if (response.getResponse() != null && response.getResponse() != "") {
				response.setStatusCode(HttpStatus.SC_OK);
				return response;
			}
    		
    		// 其次查找数据库
    		response.setResponse(getStringFromDB(context, url));
    		if (response.getResponse() != null && response.getResponse() != "") {
				response.setStatusCode(HttpStatus.SC_OK);
				return response;
			}
		} else {
			
		}
    	
        return CustomHttpClient.PostFromWebByHttpClient(context, strUrl, nameValuePairs);
    }
    
    private static 
    
	private static String getResponseFromGlobal(String url) {
		if (sRequestCache.containsKey(url)) {
			SoftReference<String> reference = sRequestCache.get(url);
			String result = (String) reference.get();
			if (result != null && !result.equals("")) {
				return result;
			}
		}
		
		return "";
	}
	
	private static String getResponseFromDB(Context context, String url) {
		long activeBeginTime = System.currentTimeMillis() - CACHE_ACTIVE_DURATION_TIME;
		return RequestCacheDBHelper.getInstance(context).getRequestCache(url, activeBeginTime);
	}

    public CustomHttpResponse getByHttpClient(Context context,
    		boolean useCache, String strUrl, NameValuePair... nameValuePairs) throws Exception {
        return CustomHttpClient.getFromWebByHttpClient(context, strUrl, nameValuePairs);
    }
}