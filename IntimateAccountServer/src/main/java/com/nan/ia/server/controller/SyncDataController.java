/**
 * @ClassName:     SyncDataController.java
 * @Description:   同步数据 
 * 
 * @author         weijiangnan create on 2015��5��16�� 
 */

package com.nan.ia.server.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.http.cmd.entities.SyncDataResponseData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.biz.EntitySwitcher;
import com.nan.ia.server.db.DBService;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.AccountCategoryTbl;
import com.nan.ia.server.db.entities.AccountRecordTbl;

@Controller
public class SyncDataController {
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@RequestMapping(value = "/sync_data"/* , method = RequestMethod.POST */)
	public @ResponseBody String syncData(HttpServletRequest request,
			Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", model);

		// 检查参数
		BoolResult<SyncDataRequestData> result = RequestHelper
				.parseRequestData(request, SyncDataRequestData.class);
		if (result.isFalse()) {
			return RequestHelper.responseParamError("");
		}

		SyncDataRequestData requestData = result.result();
		// 首先同步新账本，创建账本
		BoolResult<Map<Integer, Integer>> resultCreateNewBooks = createNewBooks(requestData);
		if (resultCreateNewBooks.isFalse()) {
			return RequestHelper.responseAccessDBError("创建账本失败。");
		}
		Map<Integer, Integer> newBookIdMap = resultCreateNewBooks.result();
		
		// 同步数据到数据库
		BoolResult<String> resultSyncDataToDB = syncDataToDB(requestData);
		if (resultSyncDataToDB.isFalse()) {
			return resultSyncDataToDB.result();
		}

		// 获取需要更新的数据
		String response = buildSyncDataFromServer(requestData, newBookIdMap);
		return response;
	}
	
	public BoolResult<Map<Integer, Integer>> createNewBooks(SyncDataRequestData requestData ) {
		DBService dbService = DBService.getInstance();
		try {
				// 插入新账本
				BoolResult<Map<Integer, Integer>> resultBookIdMap = dbService
						.insertBooks(requestData.getNewBooks());
				if (resultBookIdMap.isFalse()) {
					return BoolResult.False();
				}

				Map<Integer, Integer> newBookIdMap = resultBookIdMap.result();
				// 添加账本的创建者为账本成员
				for (int i = 0; null != requestData.getNewBooks() && i < requestData.getNewBooks().size(); i++) {
					int accountBookId = requestData.getNewBooks().get(i).getAccountBookId();
					if (newBookIdMap.containsKey(accountBookId)) {
						// 如果是新建的，创建成员
						if (!dbService.insertAccountBookMember(requestData.getNewBooks().get(i).getCreateUserId(),
								newBookIdMap.get(accountBookId))) {
							return BoolResult.False();
						};
					}
				}
				
				// 替换分类的账本id
				for (int i = 0; null != requestData.getNewCategories() && i < requestData.getNewCategories().size(); i++) {
					int accountBookId = requestData.getNewCategories().get(i).getAccountBookId();
					if (newBookIdMap.containsKey(accountBookId)) {
						// 如果是需要替换的老账本id，替换为新的账本id
						requestData.getNewCategories().get(i).setAccountBookId(newBookIdMap.get(accountBookId));
					}
				}
				
				// 替换记录的账本id
				for (int i = 0; null != requestData.getNewRecords() && i < requestData.getNewRecords().size(); i++) {
					int accountBookId = requestData.getNewRecords().get(i).getAccountBookId();
					if (newBookIdMap.containsKey(accountBookId)) {
						// 如果是需要替换的老账本id，替换为新的账本id
						requestData.getNewRecords().get(i).setAccountBookId(newBookIdMap.get(accountBookId));
					}
				}
				
				return BoolResult.True(newBookIdMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False(null);	
	}

	public BoolResult<String> syncDataToDB(SyncDataRequestData requestData) {
		DBService dbService = DBService.getInstance();
		try {
				// 更新账本
				if (!dbService.updateBooks(requestData.getUpdateBooks())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("更新账本失败。"));
				};
				
				// 删除账本
				if (!dbService.deleteBooks(requestData.getDeleteBooks(), requestData.getUserId())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("删除账本失败。"));
				};
				
				// 插入分类
				if (!dbService.insertCategories(requestData.getNewCategories())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("创插入分类失败。"));
				};
				
				// 删除分类
				if (!dbService.deleteCategories(requestData.getDeleteCategories(), requestData.getUserId())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("删除分类失败。"));
				};
				
				// 插入记录
				if (!dbService.insertRecords(requestData.getNewRecords())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("插入记录失败。"));
				}
				
				// 更新记录
				if (!dbService.updateRecords(requestData.getUpdateRecords())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("更新记录失败。"));
				}
				
				// 删除记录
				if (!dbService.deleteRecords(requestData.getDeleteRecords(), requestData.getUserId())) {
					return BoolResult.False(RequestHelper.responseAccessDBError("删除记录失败。"));
				}
				
				return BoolResult.True();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False(RequestHelper.responseAccessDBError(""));
	}
	
	public String buildSyncDataFromServer(SyncDataRequestData requestData, Map<Integer, Integer> newBookIdMap) {
		DBService dbService = DBService.getInstance();
		int userId = requestData.getUserId();
		long lastSyncDataTime = requestData.getLastSyncDataTime();

		SyncDataResponseData responseData = new SyncDataResponseData();
		
		// 是否需要重新更新账本数据
		responseData.setUpdateBooks(dbService.checkUpdateBooks(userId, lastSyncDataTime));
		if (responseData.isUpdateBooks()) {
			// 需要更新账本
			BoolResult<List<AccountBookTbl>> resultGetBookTbls = dbService.getBooksByUserId(userId);
			if (resultGetBookTbls.isFalse()) {
				return RequestHelper.responseAccessDBError("");
			}

			responseData.setBooks(EntitySwitcher.toAccountBookItems(resultGetBookTbls.result()));
			
			// 新账本ID对照
			responseData.setNewBookIdMap(newBookIdMap);
		}
		
		// 是否需要更新分类数据
		responseData.setUpdateCategories(dbService.checkUpdateCategories(userId, lastSyncDataTime));
		if (responseData.isUpdateCategories()) {
			// 需要更新账本
			BoolResult<List<AccountCategoryTbl>> resultGetCategories = dbService.getCategoriesByUserId(userId);
			if (resultGetCategories.isFalse()) {
				return RequestHelper.responseAccessDBError("");
			}

			responseData.setCategories(EntitySwitcher.toCategoryItems(resultGetCategories
					.result()));
		}
		
		// 新的记录
		BoolResult<List<AccountRecordTbl>> resultGetNewRecords = dbService.getNewRecordsByUserId(userId, lastSyncDataTime);
		if (resultGetNewRecords.isFalse()) {
			return RequestHelper.responseAccessDBError("");
		}
		responseData.setNewRecords(EntitySwitcher.toRecordItems(resultGetNewRecords.result()));
		
		// 修改的记录
		BoolResult<List<AccountRecordTbl>> resultGetUpdateRecords = dbService.getUpdateRecordsByUserId(userId, lastSyncDataTime);
		if (resultGetUpdateRecords.isFalse()) {
			return RequestHelper.responseAccessDBError("");
		}
		responseData.setUpdateRecords(EntitySwitcher.toRecordItems(resultGetUpdateRecords.result()));
		
		// 删除的记录
		BoolResult<List<Integer>> resultGetDeleteRecordIds = dbService.getDeleteRecordIdsByUserId(userId, lastSyncDataTime);
		if (resultGetDeleteRecordIds.isFalse()) {
			return RequestHelper.responseAccessDBError("");
		}
		responseData.setDeleteRecordIds(resultGetDeleteRecordIds.result());
		
		// 更新最后更新时间
		responseData.setLastSyncDataTime(System.currentTimeMillis());
		
		return RequestHelper.responseSuccess(responseData);
	}}