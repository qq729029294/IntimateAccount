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
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.entities.UserInfo;
import com.nan.ia.server.db.entities.AccountBookDeleteTbl;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.AccountCategoryDeleteTbl;
import com.nan.ia.server.db.entities.AccountCategoryDeleteTblId;
import com.nan.ia.server.db.entities.AccountCategoryTbl;
import com.nan.ia.server.db.entities.AccountCategoryTblId;
import com.nan.ia.server.db.entities.AccountRecordDeleteTbl;
import com.nan.ia.server.db.entities.AccountRecordTbl;
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
	
	public static List<AccountBook> toAccountBookItems(List<AccountBookTbl> tbls) {
		List<AccountBook> items = new ArrayList<AccountBook>();
		if (null != tbls) {
			for (int i = 0; i < tbls.size(); i++) {
				items.add(toItem(tbls.get(i)));
			}
		}
		
		return items;
	}
	
	public static AccountCategory toItem(AccountCategoryTbl tbl) {
		if (null == tbl) {
			return null;
		}
		
		AccountCategory item = new AccountCategory();
		item.setAccountBookId(tbl.getId().getAccountBookId());
		item.setCategory(tbl.getId().getCategory());
		item.setSuperCategory(tbl.getSuperCategory());
		item.setIcon(tbl.getIcon());
		item.setCreateTime(tbl.getCreateTime());
		item.setUpdateTime(tbl.getUpdateTime());
		
		return item;
	}
	
	public static List<AccountCategory> toCategoryItems(List<AccountCategoryTbl> tbls) {
		List<AccountCategory> items = new ArrayList<AccountCategory>();
		if (null != tbls) {
			for (int i = 0; i < tbls.size(); i++) {
				items.add(toItem(tbls.get(i)));
			}
		}
		
		return items;
	}
	
	public static AccountRecord toItem(AccountRecordTbl tbl) {
		if (null == tbl) {
			return null;
		}
		
		AccountRecord item = new AccountRecord();
		item.setAccountRecordId(tbl.getAccountRecordId());
		item.setAccountBookId(tbl.getAccountBookId());
		item.setCategory(tbl.getCategory());
		item.setWaterValue(tbl.getWaterValue());
		item.setDescription(tbl.getDescription());
		item.setRecordTime(tbl.getRecordTime());
		item.setRecordUserId(tbl.getRecordUserId());
		item.setCreateTime(tbl.getCreateTime());
		item.setUpdateTime(tbl.getUpdateTime());
		
		return item;
	}
	
	public static List<AccountRecord> toRecordItems(List<AccountRecordTbl> tbls) {
		List<AccountRecord> items = new ArrayList<AccountRecord>();
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
	
	public static List<UserInfo> toUserItems(List<UserTbl> tbls) {
		List<UserInfo> items = new ArrayList<UserInfo>();
		if (null != tbls) {
			for (int i = 0; i < tbls.size(); i++) {
				items.add(toItem(tbls.get(i)));
			}
		}
		
		return items;
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
		tbl.setIcon(item.getIcon());
		
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
	
	public static AccountCategoryDeleteTbl toTblForDB(AccountCategory item, int deleteUserId) {
		if (null == item) {
			return null;
		}
		
		AccountCategoryDeleteTbl tbl = new AccountCategoryDeleteTbl();
		AccountCategoryDeleteTblId tblId = new AccountCategoryDeleteTblId();
		tblId.setAccountBookId(item.getAccountBookId());
		tblId.setCategory(item.getCategory());
		tbl.setId(tblId);
		tbl.setDeleteUserId(deleteUserId);
		
		return tbl;
	}
	
	public static List<AccountCategoryDeleteTbl> toCategoryDeleteTblsForDB(List<AccountCategory> items, int deleteUserId) {
		List<AccountCategoryDeleteTbl> tbls = new ArrayList<AccountCategoryDeleteTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i), deleteUserId));
			}
		}
		
		return tbls;
	}
	
	public static AccountBookTbl toTblForDB(AccountBook item) {
		AccountBookTbl tbl = new AccountBookTbl();
		tbl.setAccountBookId(item.getAccountBookId());
		tbl.setName(item.getName());
		tbl.setDescription(item.getDescription());
		tbl.setCreateUserId(item.getCreateUserId());

		return tbl;
	}
	
	public static List<AccountBookTbl> toAccountBookTblsForDB(List<AccountBook> items) {
		List<AccountBookTbl> tbls = new ArrayList<AccountBookTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i)));
			}
		}
		
		return tbls;
	}
	
	public static AccountBookDeleteTbl toTblForDB(AccountBook item, int deleteUserId) {
		AccountBookDeleteTbl tbl = new AccountBookDeleteTbl();
		tbl.setAccountBookId(item.getAccountBookId());
		tbl.setDeleteUserId(deleteUserId);

		return tbl;
	}
	
	public static List<AccountBookDeleteTbl> toAccountBookDeleteTblsForDB(List<AccountBook> items, int deleteUserId) {
		List<AccountBookDeleteTbl> tbls = new ArrayList<AccountBookDeleteTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i), deleteUserId));
			}
		}
		
		return tbls;
	}
	
	public static AccountRecordTbl toTblForDB(AccountRecord item) {
		if (null == item) {
			return null;
		}
		
		AccountRecordTbl tbl = new AccountRecordTbl();
		tbl.setAccountRecordId(item.getAccountBookId());
		tbl.setAccountBookId(item.getAccountBookId());
		tbl.setCategory(item.getCategory());
		tbl.setWaterValue(item.getWaterValue());
		tbl.setDescription(item.getDescription());
		tbl.setRecordTime(item.getRecordTime());
		tbl.setRecordUserId(item.getRecordUserId());
		
		return tbl;
	}
	
	public static List<AccountRecordTbl> toRecordTblsForDB(List<AccountRecord> items) {
		List<AccountRecordTbl> tbls = new ArrayList<AccountRecordTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i)));
			}
		}
		
		return tbls;
	}
	
	public static AccountRecordDeleteTbl toTblForDB(AccountRecord item, int deleteUserId) {
		if (null == item) {
			return null;
		}
		
		AccountRecordDeleteTbl tbl = new AccountRecordDeleteTbl();
		tbl.setAccountRecordId(item.getAccountBookId());
		tbl.setDeleteUserId(deleteUserId);
		tbl.setCreateTime(item.getCreateTime());
		
		return tbl;
	}
	
	public static List<AccountRecordDeleteTbl> toRecordDeleteTblsForDB(List<AccountRecord> items, int deleteUserId) {
		List<AccountRecordDeleteTbl> tbls = new ArrayList<AccountRecordDeleteTbl>();
		if (null != items) {
			for (int i = 0; i < items.size(); i++) {
				tbls.add(toTblForDB(items.get(i), deleteUserId));
			}
		}
		
		return tbls;
	}
}