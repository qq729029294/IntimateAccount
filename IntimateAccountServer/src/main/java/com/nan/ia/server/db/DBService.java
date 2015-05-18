package com.nan.ia.server.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.server.db.entities.AccountBookDeleteTbl;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.UserTbl;

public class DBService {
	static private DBService sInstance; 
	static public DBService getInstance() {
		if (null == sInstance) {
			sInstance = new DBService();
		} 
		
		return sInstance;
	}
	
	public int getUserCount() {
		try {
			String hqlString = "select count(*) from UserTbl";  
		    Query query = HibernateUtil.getSession().createQuery(hqlString);
		    
		    return ((Number)query.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public boolean registerUser(String username, String password, String nickname) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction trans = session.beginTransaction();
			
			UserTbl userTbl = new UserTbl();
			userTbl.setUserId(0);
			userTbl.setUsername(username);
			userTbl.setPassword(password);
			userTbl.setNickname(nickname);
			userTbl.setCreateTime(new Timestamp(System.currentTimeMillis()));
			
			session.save(userTbl);
			
			trans.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean syncData(SyncDataRequestData requestData) {
		try {
			// 新账本
			List<AccountBookTbl> newAccountBookTbls = new ArrayList<AccountBookTbl>();
			for (int i = 0; i < requestData.getNewAccountBooks().size(); i++) {
				AccountBook item = requestData.getNewAccountBooks().get(i);
				
				AccountBookTbl tbl = new AccountBookTbl();
				tbl.setAccountBookId(0);
				tbl.setName(item.getName());
				tbl.setDescription(item.getDescription());
				tbl.setCreateUserId(item.getCreateUserId());
				
				newAccountBookTbls.add(tbl);
			}
			
			// 修改账本
			List<AccountBookTbl> updateAccountBookTbls = new ArrayList<AccountBookTbl>();
			for (int i = 0; i < requestData.getUpdateAccountBooks().size(); i++) {
				AccountBook item = requestData.getUpdateAccountBooks().get(i);
				
				AccountBookTbl tbl = new AccountBookTbl();
				tbl.setAccountBookId(item.getAccountBookId());
				tbl.setName(item.getName());
				tbl.setDescription(item.getDescription());
				tbl.setCreateUserId(item.getCreateUserId());
				
				updateAccountBookTbls.add(tbl);
			}
			
			// 删除账本
			List<AccountBookDeleteTbl> accountBookDeleteTbls = new ArrayList<AccountBookDeleteTbl>();
			for (int i = 0; i < requestData.getAccountBookDeletes().size(); i++) {
				AccountBookDelete item = requestData.getAccountBookDeletes().get(i);
				
				AccountBookDeleteTbl tbl = new AccountBookDeleteTbl();
				tbl.setAccountBookId(item.getAccountBookId());
				tbl.setDeleteUserId(item.getDeleteUserId());
				
				accountBookDeleteTbls.add(tbl);
			}
			
			// 修改数据库
			Session session = HibernateUtil.getSession();
			Transaction trans = session.beginTransaction();
			for (int i = 0; i < newAccountBookTbls.size(); i++) {
				session.save(newAccountBookTbls.get(i));
			}
			
			for (int i = 0; i < updateAccountBookTbls.size(); i++) {
				session.update(updateAccountBookTbls.get(i));
			}
			
			for (int i = 0; i < accountBookDeleteTbls.size(); i++) {
				session.delete(accountBookDeleteTbls.get(i));
				// 同时删除账本下的类别
				deleteCategoryByAccountBookId(accountBookDeleteTbls.get(i).getAccountBookId());
				// 同时删除账本下的账目
				deleteAccountItemByAccountBookId(accountBookDeleteTbls.get(i).getAccountBookId());
			}
			
			trans.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 检查是否需要更新账本信息
	 * @return
	 */
	public boolean checkNeedUpdateAccountBooks(int userId, long lastUpdateTime, boolean dest) {
		dest = false;
		
		try {
			Session session = HibernateUtil.getSession();
			// 检查原表是否有更新
			Query query = session.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.memberUserId = ? AND r.accountBookId = s.accountBookId AND r.updateTime > ?");
			query.setParameter(0, new Date(userId));
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				dest = true;
				return true;
			};
			
			// 检查删除表是否有新删除
			query = session.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s, AccountBookDeleteTbl t WHERE s.memberUserId = ? AND r.accountBookId = s.accountBookId AND s.accountBookId = t.accountBookId AND s.deleteTime > ?");
			query.setParameter(0, new Date(userId));
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				dest = true;
				return true;
			};
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 获得用户的所有账本信息
	 * @param lastUpdateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getAccountBooksByUserId(int userId, List<AccountBookTbl> dest) {
		dest = new ArrayList<AccountBookTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("SELECT r FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.memberUserId = ? AND r.accountBookId = s.accountBookId");
			query.setParameter(0, userId);
			dest.addAll(query.list());
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean deleteCategoryByAccountBookId(int accountBookId) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("DELETE FROM AccountCategoryTbl r WHERE r.accountBookId = ?");
			query.setParameter(0, accountBookId);
			query.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean deleteAccountItemByAccountBookId(int accountBookId) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("DELETE FROM AccountItemTbl r WHERE r.accountBookId = ?");
			query.setParameter(0, accountBookId);
			query.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
