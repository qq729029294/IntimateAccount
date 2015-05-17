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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;

@Controller
public class SyncDataController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/sync_data", method = RequestMethod.GET)
	public @ResponseBody String syncData(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		SyncDataRequestData requestData = null;
		if (!RequestHelper.parseRequestData(request, requestData, SyncDataRequestData.class)) {
			return RequestHelper.responseParamError(SyncDataRequestData.class);
		};
		
		// 新账本
		
		return "lao po chi fan";
	}
}