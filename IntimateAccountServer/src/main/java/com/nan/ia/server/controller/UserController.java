/**
 * @ClassName:     SyncDataController.java
 * @Description:   同步数据 
 * 
 * @author         weijiangnan create on 2015��5��16�� 
 */

package com.nan.ia.server.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.RegisterRequestData;
import com.nan.ia.common.http.cmd.entities.VerifyMailRequestData;
import com.nan.ia.common.http.cmd.entities.VerifyVfCodeRequestData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.biz.BizFacade;
import com.nan.ia.server.constant.Constant;
import com.nan.ia.server.db.DBService;

@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/verify_mail"/*, method = RequestMethod.POST*/)
	public @ResponseBody String verifyMail(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		BoolResult<VerifyMailRequestData> result = RequestHelper.parseRequestData(request, VerifyMailRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		VerifyMailRequestData requestData = result.result();
		
		// 判断是否已经存在用户
		if (DBService.getInstance().exitUsername(requestData.getMail())) {
			// 邮箱已经被注册
			return RequestHelper.responseError(ServerErrorCode.RET_MAIL_ALREADY_REGISTER, "该邮箱已经被注册");
		}
		
		// 发送验证邮件
		if (!BizFacade.getInstance().sendVfCodeByMail(requestData.getMail())) {
			// 发送验证邮件失败
			return RequestHelper.responseError(ServerErrorCode.RET_SEND_MAIL_FAIL, "发送验证邮件失败，请稍后再试");
		};
		
		return RequestHelper.responseSuccess();
	}
	
	@RequestMapping(value = "/verify_vf_code"/*, method = RequestMethod.POST*/)
	public @ResponseBody String verifyVfCode(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		BoolResult<VerifyVfCodeRequestData> result =
				RequestHelper.parseRequestData(request, VerifyVfCodeRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		VerifyVfCodeRequestData requestData = result.result();
		
		BoolResult<Object> verificationCodeResult =
				BizFacade.getInstance().verificationCode(requestData.getUsername(), requestData.getVfCode());
		if (verificationCodeResult.isFalse()) {
			// 验证码无效
			return RequestHelper.responseError(ServerErrorCode.RET_INVALID_VF_CODE,
					verificationCodeResult.errMsg());
		}
		
		return RequestHelper.responseSuccess();
	}
	
	@RequestMapping(value = "/register"/*, method = RequestMethod.POST*/)
	public @ResponseBody String register(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		BoolResult<RegisterRequestData> result =
				RequestHelper.parseRequestData(request, RegisterRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		RegisterRequestData requestData = result.result();
		// 再次验证验证码
		if (requestData.getAccountType() == Constant.ACCOUNT_TYPE_MAIL) {
			BoolResult<Object> verificationCodeResult =
					BizFacade.getInstance().verificationCode(requestData.getUsername(), requestData.getVfCode());
			if (verificationCodeResult.isFalse()) {
				// 验证码无效
				return RequestHelper.responseError(ServerErrorCode.RET_INVALID_VF_CODE,
						verificationCodeResult.errMsg());
			}
		}
		
		// 注册用户
		if (!DBService.getInstance().registerUser(requestData.getUsername(),
				requestData.getPassword(), requestData.getAccountType())) {
			return RequestHelper.responseAccessDBError("");
		}
		
		return RequestHelper.responseSuccess();
	}
}