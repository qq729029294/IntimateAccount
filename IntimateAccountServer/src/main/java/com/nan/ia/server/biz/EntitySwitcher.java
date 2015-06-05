/**
 * @ClassName:     EntitySwitcher.java
 * @Description:   实体类转换
 * 
 * @author         weijiangnan create on 2015年5月18日 
 */

package com.nan.ia.server.biz;

import java.util.ArrayList;
import java.util.List;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountCategoryDelete;
import com.nan.ia.common.entities.UserInfo;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.AccountCategoryDeleteTbl;
import com.nan.ia.server.db.entities.AccountCategoryDeleteTblId;
import com.nan.ia.server.db.entities.AccountCategoryTbl;
import com.nan.ia.server.db.entities.AccountCategoryTblId;
import com.nan.ia.server.db.entities.UserTbl;

public class EntitySwitcher {
	public static AccountBook toItem(AccountBookTbl tbl) {
		if (null == tbl) {
			return null;
		}
		
		AccountBook item = new AccountBook();
		item.setAccountBookId(tbl.getAccountBookId());
		item.setCreateTime(tbl.getCreateTime());
		item.setCreateUserId(tbl.getCreateUserId());
		item.setDescription(tbl.getDescription());
		item.setName(tbl.getName());
		item.setUpdateTime(tbl.getUpdateTime());
		
		return item;
	}
	
	public static List<AccountBook> toItems(List<AccountBookTbl> tbls) {
		List<AccountBook> items = new ArrayList<AccountBook>();
		if (null != tbls) {
			for (int i = 0; i < tbls.size(); i++) {
				items.add(toItem(tbls.get(i)));
			}
		}
		
		return items;
	}
	
	public static UserInfo toItem(UserTbl tbl) {
		if (null == tbl) {
			return null;
		}
		
		UserInfo item = new UserInfo();
		item.setUserId(tbl.getUserId());
		item.setNickname(tbl.getNickname());
		item.setAvatarUrl(tbl.getAvatar());
		
		return item;
	}
	
	public static AccountCategoryTbl toTblForDB(AccountCategory item) {
		if (null == item) {
			return null;
		}
		
		AccountCategoryTbl tbl = new AccountCategoryTbl();
		AccountCategoryTblId tblId = new AccountCategoryTblId();
		tblId.setAccountBookId(item.getAccountBookId());
		tblId.setCategory(item.getCategory());
		tbl.setId(tblId);
		tbl.setSuperCategory(item.getSuperCategory());
		
		return tbl;
	}
	
	public static List<AccountCategoryTbl> toCategoryTblsForDB(List<AccountCategory> items) {
		List<AccountCategoryTbl> tbls = new ArrayList<AccountCategoryTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i)));
			}
		}
		
		return tbls;
	}
	
	public static AccountCategoryDeleteTbl toTblForDB(AccountCategoryDelete item) {
		if (null == item) {
			return null;
		}
		
		AccountCategoryDeleteTbl tbl = new AccountCategoryDeleteTbl();
		AccountCategoryDeleteTblId tblId = new AccountCategoryDeleteTblId();
		tblId.setAccountBookId(item.getAccountBookId());
		tblId.setCategory(item.getCategory());
		tbl.setId(tblId);
		tbl.setDeleteUserId(item.getDeleteUserId());
		
		return tbl;
	}
	
	public static List<AccountCategoryDeleteTbl> toCategoryDeleteTblsForDB(List<AccountCategoryDelete> items) {
		List<AccountCategoryDeleteTbl> tbls = new ArrayList<AccountCategoryDeleteTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i)));
			}
		}
		
		return tbls;
	}
}