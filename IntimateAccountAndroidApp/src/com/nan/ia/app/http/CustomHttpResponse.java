package com.nan.ia.app.http;

/**
 * @author Create by weijiangnan on 2015-5-13
 */
public class CustomHttpResponse {
	public static final int HTTP_ERROR = -1;
	
	private int statusCode;
	private String response;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
}
