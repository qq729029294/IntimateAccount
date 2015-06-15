/**
 * @ClassName:     Url.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.app.constant;

public class Url {
	public static final String URL_PREFIX = "http://192.168.11.207:8080/ia";
//	public static final String URL_PREFIX = "http://10.235.6.93:8080/ia";
	public static final String URL_SYNC_DATA = URL_PREFIX + "/sync_data";
	
	// 用户相关
	public static final String URL_VERIFY_MAIL = URL_PREFIX + "/verify_mail";
	public static final String URL_VERIFY_VF_CODE = URL_PREFIX + "/verify_vf_code";
	public static final String URL_REGISTER = URL_PREFIX + "/register";
	public static final String URL_ACCOUNT_LOGIN = URL_PREFIX + "/account_login";
	
	// 通用
	public static final String URL_PULL_ACCOUNT_BOOKS = URL_PREFIX + "/pull_account_books";
}
