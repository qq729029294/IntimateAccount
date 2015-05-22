package com.nan.ia.server.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountBookDelete;
import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.db.entities.AccountBookDeleteTbl;
import com.nan.ia.server.db.entities.AccountBookMemberTbl;
import com.nan.ia.server.db.entities.AccountBookMemberTblId;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.LoginAccountTbl;
import com.nan.ia.server.db.entities.LoginAccountTblId;
import com.nan.ia.server.db.entities.LoginStateTbl;
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
	
	public boolean registerUser(String username, String password, int accountType) {
		try {
			Session session = HibernateUtil.getSession();
			
			// 先创建新用户
			Transaction trans = session.beginTransaction();
			UserTbl userTbl = new UserTbl();
			userTbl.setUserId(0);
			userTbl.setNickname("无名小辈");
			userTbl.setDescription("很懒哦，什么都没有留下");
			session.save(userTbl);
			trans.commit();
			
			trans = session.beginTransaction();
			// 创建登录账户
			LoginAccountTbl loginAccountTbl = new LoginAccountTbl();
			LoginAccountTblId loginAccountTblId = new LoginAccountTblId();
			loginAccountTblId.setUsername(username);
			loginAccountTblId.setAccountType(accountType);
			
			loginAccountTbl.setId(loginAccountTblId);
			loginAccountTbl.setUserId(userTbl.getUserId());
			loginAccountTbl.setPassword(password);
			session.save(loginAccountTbl);
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
			if (null != requestData.getNewAccountBooks()) {
				for (int i = 0; i < requestData.getNewAccountBooks().size(); i++) {
					AccountBook item = requestData.getNewAccountBooks().get(i);
					
					AccountBookTbl tbl = new AccountBookTbl();
					tbl.setAccountBookId(0);
					tbl.setName(item.getName());
					tbl.setDescription(item.getDescription());
					tbl.setCreateUserId(item.getCreateUserId());
					
					newAccountBookTbls.add(tbl);
				}
			}
			
			// 修改账本
			List<AccountBookTbl> updateAccountBookTbls = new ArrayList<AccountBookTbl>();
			if (null != requestData.getUpdateAccountBooks()) {
				for (int i = 0; i < requestData.getUpdateAccountBooks().size(); i++) {
					AccountBook item = requestData.getUpdateAccountBooks().get(i);
					
					AccountBookTbl tbl = new AccountBookTbl();
					tbl.setAccountBookId(item.getAccountBookId());
					tbl.setName(item.getName());
					tbl.setDescription(item.getDescription());
					tbl.setCreateUserId(item.getCreateUserId());
					
					updateAccountBookTbls.add(tbl);
				}
			}
			
			// 删除账本
			List<AccountBookDeleteTbl> accountBookDeleteTbls = new ArrayList<AccountBookDeleteTbl>();
			if (null != requestData.getAccountBookDeletes()) {
				for (int i = 0; i < requestData.getAccountBookDeletes().size(); i++) {
					AccountBookDelete item = requestData.getAccountBookDeletes().get(i);
					
					AccountBookDeleteTbl tbl = new AccountBookDeleteTbl();
					tbl.setAccountBookId(item.getAccountBookId());
					tbl.setDeleteUserId(item.getDeleteUserId());
					
					accountBookDeleteTbls.add(tbl);
				}
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
			
			// 添加书的创建者为书的成员
			for (int i = 0; i < newAccountBookTbls.size(); i++) {
				insertAccountBookMember(newAccountBookTbls.get(i).getCreateUserId(),
						newAccountBookTbls.get(i).getAccountBookId());
			}
			
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
	public boolean checkNeedUpdateAccountBooks(int userId, long lastUpdateTime) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查原表是否有更新
			Query query = session.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND r.updateTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				return true;
			};
			
			// 检查删除表是否有新删除
			query = session.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s, AccountBookDeleteTbl t WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND s.id.accountBookId = t.accountBookId AND t.deleteTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
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
	public BoolResult<List<AccountBookTbl>> getAccountBooksByUserId(int userId) {
		List<AccountBookTbl> accountBookTbls = new ArrayList<AccountBookTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("SELECT r FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId");
			query.setParameter(0, userId);
			accountBookTbls.addAll(query.list());
			
			return BoolResult.True(accountBookTbls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return BoolResult.False();
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
	
	public boolean insertAccountBookMember(int userId, int accountBookId) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction trans = session.beginTransaction();
			
			AccountBookMemberTbl tbl = new AccountBookMemberTbl();
			AccountBookMemberTblId tblId = new AccountBookMemberTblId();
			tblId.setMemberUserId(userId);
			tblId.setAccountBookId(accountBookId);
			tbl.setId(tblId);
			session.save(tbl);
			
			trans.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean existUsername(String username) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查是否已经存在用户名
			Query query = session.createQuery("FROM LoginAccountTbl r WHERE r.id.username = ?");
			query.setParameter(0, username);
			if (query.list().size() > 0) {
				return true;
			};
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public BoolResult<LoginAccountTbl> getLoginAccount(String username, int accountType) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查是否已经存在用户名
			Query query = session.createQuery("FROM LoginAccountTbl r WHERE r.id.username = ? AND r.id.accountType = ?");
			query.setParameter(0, username);
			query.setParameter(1, accountType);
			
			List<LoginAccountTbl> tbls = query.list();
			if (tbls.size() == 1) {
				return BoolResult.True(tbls.get(0));
			} else {
				return BoolResult.True(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return BoolResult.False();
	}
	
	public boolean updateLoginState(int userId, String token) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction trans = session.beginTransaction();
			LoginStateTbl tbl = new LoginStateTbl();
			tbl.setUserId(userId);
			tbl.setToken(token);
			
			session.saveOrUpdate(tbl);
			trans.commit();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}