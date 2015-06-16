/**
 * @ClassName:     AccountBookController.java
 * @Description:   账本相关 
 * 
 * @author         weijiangnan create on 2015��5��16�� 
 */

package com.nan.ia.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.entities.InviteMemberInfo;
import com.nan.ia.common.entities.UserInfo;
import com.nan.ia.common.http.cmd.entities.AgreeInviteMemberRequestData;
import com.nan.ia.common.http.cmd.entities.InviteMemberRequestData;
import com.nan.ia.common.http.cmd.entities.PullMsgsRequestData;
import com.nan.ia.common.http.cmd.entities.PullMsgsResponseData;
import com.nan.ia.common.http.cmd.entities.PullUserInfosRequestData;
import com.nan.ia.common.http.cmd.entities.PullUserInfosResponseData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.biz.BizFacade;
import com.nan.ia.server.biz.EntitySwitcher;
import com.nan.ia.server.db.DBService;
import com.nan.ia.server.db.entities.AccountBookMemberTbl;
import com.nan.ia.server.db.entities.AccountTbl;
import com.nan.ia.server.db.entities.UserTbl;

@Controller
public class MemberController {
	
	@RequestMapping(value = "/invite_member"/*, method = RequestMethod.POST*/)
	public @ResponseBody String inviteMember(HttpServletRequest request, Locale locale, Model model) {
		// 检查参数
		BoolResult<InviteMemberRequestData> result = RequestHelper.parseRequestData(request, InviteMemberRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		InviteMemberRequestData requestData = result.result();
		
		// 查询用户信息
		BoolResult<AccountTbl> resultGetAccount = DBService.getInstance().getAccount(requestData.getInviteeUsername());
		if (resultGetAccount.isFalse()) {
			return RequestHelper.responseAccessDBError("");
		}
		if (resultGetAccount.result() == null) {
			return RequestHelper.responseError(ServerErrorCode.RET_USERNAME_NOT_EXIST, "用户名不存在");
		}
		
		int inviteeUserId = resultGetAccount.result().getUserId();
		
		InviteMemberInfo info = new InviteMemberInfo();
		info.setInviterUserId(requestData.getUserId());
		info.setInviteeUserId(inviteeUserId);
		info.setAccountBookId(requestData.getAccountBookId());
		info.setAccountBookName(requestData.getAccountBookName());
		
		BizFacade.getInstance().pushInviteMemberInfos(inviteeUserId, info);

		return RequestHelper.responseSuccess();
	}
	
	@RequestMapping(value = "/agree_invite_member"/*, method = RequestMethod.POST*/)
	public @ResponseBody String agreeInviteMember(HttpServletRequest request, Locale locale, Model model) {
		// 检查参数
		BoolResult<AgreeInviteMemberRequestData> result = RequestHelper.parseRequestData(request, AgreeInviteMemberRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		AgreeInviteMemberRequestData requestData = result.result();
		
		List<AccountBookMemberTbl> memberTbls = DBService.getInstance().getBookMembers(requestData.getAccountBookId());
		for (int i = 0; i < memberTbls.size(); i++) {
			if (memberTbls.get(i).getId().getMemberUserId() == requestData.getUserId()) {
				return RequestHelper.responseError(ServerErrorCode.RET_HAS_BEEN_MEMBER, "已经是改账本的成员");
			}
		}
		
		// 加入成员
		if (!DBService.getInstance().insertAccountBookMember(requestData.getUserId(), requestData.getAccountBookId())) {
			return RequestHelper.responseAccessDBError("添加成员出现异常");
		};
		
		return RequestHelper.responseSuccess();
	}
	
	@RequestMapping(value = "/pull_msgs"/*, method = RequestMethod.POST*/)
	public @ResponseBody String pullMsgs(HttpServletRequest request, Locale locale, Model model) {

		// 检查参数
		BoolResult<PullMsgsRequestData> result = RequestHelper.parseRequestData(request, PullMsgsRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		PullMsgsRequestData requestData = result.result();
		PullMsgsResponseData responseData = new PullMsgsResponseData();
		responseData.setInviteMemberInfos(BizFacade.getInstance().popInviteMemberInfos(requestData.getUserId()));
		
		return RequestHelper.responseSuccess(responseData);
	}
	
	@RequestMapping(value = "/pull_user_infos"/*, method = RequestMethod.POST*/)
	public @ResponseBody String pullUserInfos(HttpServletRequest request, Locale locale, Model model) {

		// 检查参数
		BoolResult<PullUserInfosRequestData> result = RequestHelper.parseRequestData(request, PullUserInfosRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		PullUserInfosRequestData requestData = result.result();
		
		PullUserInfosResponseData responseData = new PullUserInfosResponseData();
		if (requestData.getUserIds().size() > 0) {
			// 更新相关成员的信息
			BoolResult<List<UserTbl>> resultGetUsers = DBService.getInstance().getUsers(requestData.getUserIds());
			if (resultGetUsers.isFalse()) {
				return RequestHelper.responseAccessDBError("");
			}
			
			responseData.setUserInfos(EntitySwitcher.toUserItems(resultGetUsers.result()));
		} else {
			responseData.setUserInfos(new ArrayList<UserInfo>());
		}
		
		return RequestHelper.responseSuccess(responseData);
	}
}