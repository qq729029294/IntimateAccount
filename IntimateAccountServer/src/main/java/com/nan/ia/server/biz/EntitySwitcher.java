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
import com.nan.ia.common.entities.UserInfo;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.UserTbl;

public class EntitySwitcher {
	public static AccountBook fromTbl(AccountBookTbl tbl) {
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
	
	public static List<AccountBook> fromTbls(List<AccountBookTbl> tbls) {
		if (null == tbls) {
			return null;
		}
		
		List<AccountBook> items = new ArrayList<AccountBook>();
		for (int i = 0; i < tbls.size(); i++) {
			items.add(fromTbl(tbls.get(i)));
		}
		
		return items;
	}
	
	public static UserInfo fromTbl(UserTbl tbl) {
		if (null == tbl) {
			return null;
		}
		
		UserInfo item = new UserInfo();
		item.setUserId(tbl.getUserId());
		item.setNickname(tbl.getNickname());
		item.setAvatarUrl(tbl.getAvatar());
		
		return item;
	}
}