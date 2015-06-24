/**
 * @ClassName:     Url.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月16日 
 */

package com.nan.ia.app.constant;

public class Url {
	public static final String URL_PREFIX = "http://192.168.11.207:8080/ia";
//	public static final String URL_PREFIX = "http://115.28.213.227:8080/ia";
	
	// 同步数据
	public static final String URL_SYNC_DATA = URL_PREFIX + "/sync_data";
	
	// 用户相关
	public static final String URL_VERIFY_MAIL = URL_PREFIX + "/verify_mail";
	public static final String URL_VERIFY_VF_CODE = URL_PREFIX + "/verify_vf_code";
	public static final String URL_REGISTER = URL_PREFIX + "/register";
	public static final String URL_ACCOUNT_LOGIN = URL_PREFIX + "/account_login";
	
	// 通用
	public static final String URL_PULL_ACCOUNT_BOOKS = URL_PREFIX + "/pull_account_books";
	
	// 账本成员
	public static final String URL_INVITE_MEMBER = URL_PREFIX + "/invite_member";
	public static final String URL_AGREE_INVITE_MEMBER = URL_PREFIX + "/agree_invite_member";
	public static final String URL_PULL_MSGS = URL_PREFIX + "/pull_msgs";
	public static final String URL_PULL_USER_INFOS = URL_PREFIX + "/pull_user_infos";
}
