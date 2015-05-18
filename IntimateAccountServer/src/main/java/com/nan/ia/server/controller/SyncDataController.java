/**
 * @ClassName:     SyncDataController.java
 * @Description:   同步数据 
 * 
 * @author         weijiangnan create on 2015��5��16�� 
 */

package com.nan.ia.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.http.cmd.entities.SyncDataResponseData;
import com.nan.ia.server.biz.EntitySwitcher;
import com.nan.ia.server.db.DBService;
import com.nan.ia.server.db.entities.AccountBookTbl;

@Controller
public class SyncDataController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/sync_data", method = RequestMethod.POST)
	public @ResponseBody String syncData(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		SyncDataRequestData requestData = null;
		if (!RequestHelper.parseRequestData(request, requestData, SyncDataRequestData.class)) {
			return RequestHelper.responseParamError("");
		};
		
		// 同步数据到数据库
		if (!DBService.getInstance().syncData(requestData)) {
			return RequestHelper.responseAccessDBError("");
		}
		
		SyncDataResponseData responseData = new SyncDataResponseData();
		// 更新最后更新时间
		responseData.setLastSyncDataServerTime(System.currentTimeMillis());
		
		int userId = RequestHelper.getUserIdFromToken(requestData.getToken());
		// 是否需要重新更新账本数据
		boolean needUpdateAccountBooks = false;
		if (!DBService.getInstance().checkNeedUpdateAccountBooks(userId, requestData.getLastSyncDataServerTime(), needUpdateAccountBooks)) {
			return RequestHelper.responseAccessDBError("");
		}
		
		responseData.setUpdateAccountBooks(needUpdateAccountBooks);
		if (needUpdateAccountBooks) {
			// 需要更新账本
			List<AccountBookTbl> tbls = null;
			if (!DBService.getInstance().getAccountBooksByUserId(userId, tbls)) {
				return RequestHelper.responseAccessDBError("");
			};
			
			responseData.setAccountBooks(EntitySwitcher.fromTbls(tbls));
		}
		
		return RequestHelper.responseSuccess(responseData);
	}
}