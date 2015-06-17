/**
 * @ClassName:     AccountBookController.java
 * @Description:   账本相关 
 * 
 * @author         weijiangnan create on 2015��5��16�� 
 */

package com.nan.ia.server.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.http.cmd.entities.PullAccountBooksRequestData;
import com.nan.ia.common.http.cmd.entities.PullAccountBooksResponseData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.biz.EntitySwitcher;
import com.nan.ia.server.db.DBService;
import com.nan.ia.server.db.entities.AccountBookTbl;

@Controller
public class CommonController {
	
	@RequestMapping(value = "/pull_account_books"/*, method = RequestMethod.POST*/)
	public @ResponseBody String pullAccountBooks(HttpServletRequest request, Locale locale, Model model) {

		// 检查参数
		BoolResult<PullAccountBooksRequestData> result = RequestHelper.parseRequestData(request, PullAccountBooksRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		};
		
		PullAccountBooksRequestData requestData = result.result();
		
		// 查询账本信息
		BoolResult<List<AccountBookTbl>> resultGetBooks = DBService.getInstance().getBooksByUserId(requestData.getUserId());
		if (resultGetBooks.isFalse()) {
			return RequestHelper.responseAccessDBError("拉取账本出现异常");
		}
		
		PullAccountBooksResponseData responseData = new PullAccountBooksResponseData();
		responseData.setBooks(EntitySwitcher.toAccountBookItems(resultGetBooks.result()));
		
		return RequestHelper.responseSuccess(responseData);
	}
}