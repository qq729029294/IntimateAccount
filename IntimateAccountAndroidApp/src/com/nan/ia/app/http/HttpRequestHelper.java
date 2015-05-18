package com.nan.ia.app.http;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;

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
    		String urlKey = makeUrlKey("POST", url, nameValuePairs);
    		response.setResponse(getResponseFromCache(context, urlKey));
    		if (response.getResponse() != null && response.getResponse() != "") {
    			response.setStatusCode(HttpStatus.SC_OK);
    			return response;
    		}
		}
    	
    	response = HttpUtil.postByHttpClient(context, url, nameValuePairs);
    	if (useCache && response.getStatusCode() == HttpStatus.SC_OK) {
    		String urlKey = makeUrlKey("POST", url, nameValuePairs);
			HttpRequestHelper.saveResponseToCache(context, urlKey, response.getResponse());
		}
    	
        return response;
    }
    
    public static CustomHttpResponse getByHttpClient(Context context,
    		boolean useCache, String url, NameValuePair... nameValuePairs) throws Exception {
    	CustomHttpResponse response = new CustomHttpResponse();
    	String urlKey = makeUrlKey("GET", url, nameValuePairs);
    	// 使用缓存
    	if (useCache) {
    		response.setResponse(getResponseFromCache(context, urlKey));
    		if (response.getResponse() != null && response.getResponse() != "") {
    			response.setStatusCode(HttpStatus.SC_OK);
    			return response;
    		}
		}
    	
    	// 不使用缓存，或者没有缓存，从网上拉取
    	response = HttpUtil.getByHttpClient(context, url, nameValuePairs);
    	if (response.getStatusCode() == HttpStatus.SC_OK) {
				HttpRequestHelper.saveResponseToCache(context, urlKey, response.getResponse());
				}
    	
        return response;
    }
    
    private static String makeUrlKey(String httpMethod, String url, NameValuePair... nameValuePairs) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(httpMethod);
    	builder.append(":");
    	builder.append(url);
    	
		try {
	    	for (int i = 0; i < nameValuePairs.length; i++) {
	    		if (i == 0) {
	    			builder.append("?");
				}
	    		
	    		builder.append(URLEncoder.encode(nameValuePairs[i].getName(), HTTP.UTF_8));
	    		builder.append("=");
	    		builder.append(URLEncoder.encode(nameValuePairs[i].getValue(), HTTP.UTF_8));
	    		
	    		if (i + 1 < nameValuePairs.length) {
	    			builder.append("&");
	    		}
	    	}
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
    }
    
    private static void saveResponseToCache(Context context, String urlKey, String response) {
    	// 保存到内存中
    	SoftReference<String> softReferenceResponse = new SoftReference<String>(response);
    	sRequestCache.put(urlKey, softReferenceResponse);
    	// 保存到数据库中
    	RequestCacheDBHelper.getInstance(context).insertRequestCache(urlKey, response);
    }
    
    private static String getResponseFromCache(Context context, String urlKey) {
    	String response = "";
		// 首先查找内存
    	response = getResponseFromGlobal(urlKey);
		
		// 其次查找数据库
		if (response == null || response == "") {
			response = getResponseFromDB(context, urlKey);
		}
		
		return response;
    }
    
	private static String getResponseFromGlobal(String urlKey) {
		if (sRequestCache.containsKey(urlKey)) {
			SoftReference<String> reference = sRequestCache.get(urlKey);
			String result = (String) reference.get();
			if (result != null && !result.equals("")) {
				return result;
			}
		}
		
		return "";
	}
	
	private static String getResponseFromDB(Context context, String urlKey) {
		long activeBeginTime = System.currentTimeMillis() - CACHE_ACTIVE_DURATION_TIME;
		return RequestCacheDBHelper.getInstance(context).getRequestCache(urlKey, activeBeginTime);
	}
}