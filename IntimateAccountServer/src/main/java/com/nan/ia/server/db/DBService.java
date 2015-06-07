package com.nan.ia.server.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.biz.EntitySwitcher;
import com.nan.ia.server.db.entities.AccountBookDeleteTbl;
import com.nan.ia.server.db.entities.AccountBookMemberTbl;
import com.nan.ia.server.db.entities.AccountBookMemberTblId;
import com.nan.ia.server.db.entities.AccountBookTbl;
import com.nan.ia.server.db.entities.AccountCategoryDeleteTbl;
import com.nan.ia.server.db.entities.AccountCategoryTbl;
import com.nan.ia.server.db.entities.AccountRecordDeleteTbl;
import com.nan.ia.server.db.entities.AccountRecordTbl;
import com.nan.ia.server.db.entities.AccountTbl;
import com.nan.ia.server.db.entities.AccountTblId;
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

			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public boolean registerUser(String username, String password,
			int accountType) {
		try {
			Session session = HibernateUtil.getSession();

			// 先创建新用户
			Transaction trans = session.beginTransaction();
			UserTbl userTbl = new UserTbl();
			userTbl.setUserId(0);
			// 默认信息
			userTbl.setNickname("无名小辈");
			userTbl.setAvatar("http://p1.gexing.com/touxiang/2011-6/629344710201181462.jpg");
			userTbl.setDescription("很懒哦，什么都没有留下~");
			session.save(userTbl);
			trans.commit();

			trans = session.beginTransaction();
			// 创建登录账户
			AccountTbl AccountTbl = new AccountTbl();
			AccountTblId AccountTblId = new AccountTblId();
			AccountTblId.setUsername(username);
			AccountTblId.setAccountType(accountType);

			AccountTbl.setId(AccountTblId);
			AccountTbl.setUserId(userTbl.getUserId());
			AccountTbl.setPassword(password);
			session.save(AccountTbl);
			trans.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean insertCategories(List<AccountCategory> categories) {
		// 新分类
		List<AccountCategoryTbl> newCategoryTbls = EntitySwitcher.toCategoryTblsForDB(categories);
		if (newCategoryTbls.size() == 0) {
			return true;
		}

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.save(session, newCategoryTbls);
		trans.commit();
		return true;
	}
	
	public boolean deleteCategories(List<AccountCategory> deletes, int deleteUserId) {
		// 构建删除分类列表
		List<AccountCategoryTbl> deleteCategoryTbls = EntitySwitcher.toCategoryTblsForDB(deletes);
		if (deleteCategoryTbls.size() == 0) {
			return true;
		}
 
		// 需要添加到分类删除表中
		List<AccountCategoryDeleteTbl> categoryDeleteTbls = EntitySwitcher.toCategoryDeleteTblsForDB(deletes, deleteUserId);
		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		
		for (int i = 0; i < deleteCategoryTbls.size(); i++) {
			// 修改记录中的类别为父类别
			AccountCategoryTbl categoryTbl = deleteCategoryTbls.get(i);
			if (categoryTbl.getSuperCategory() != null && !categoryTbl.getSuperCategory().isEmpty()) {
				updateRecordCategory(categoryTbl.getId().getAccountBookId(),
						categoryTbl.getId().getCategory(),
						categoryTbl.getSuperCategory());
			}
		}
		
		// 删除分类数据
		HibernateUtil.delete(session, deleteCategoryTbls);
		// 添加分类删除表中的数据
		HibernateUtil.save(session, categoryDeleteTbls);
		
		trans.commit();
		return true;
	}
	
	public boolean insertRecords(List<AccountRecord> records) {
		// 新记录
		List<AccountRecordTbl> newRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (newRecordTbls.size() == 0) {
			return true;
		}

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.save(session, newRecordTbls);
		trans.commit();
		return true;
	}
	
	public boolean updateRecords(List<AccountRecord> records) {
		// 新记录
		List<AccountRecordTbl> updateRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (updateRecordTbls.size() == 0) {
			return true;
		}

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.update(session, updateRecordTbls);
		trans.commit();
		return true;
	}
	
	public boolean deleteRecords(List<AccountRecord> records, int deleteUserId) {
		// 构建删除记录数据
		List<AccountRecordTbl> deleteRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (deleteRecordTbls.size() == 0) {
			return true;
		}
 
		// 需要添加到记录删除表中
		List<AccountRecordDeleteTbl> recordDeleteTbls = EntitySwitcher.toRecordDeleteTblsForDB(records, deleteUserId);
		
		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		// 删除记录数据
		HibernateUtil.delete(session, deleteRecordTbls);
		// 添加记录删除表中的数据
		HibernateUtil.save(session, recordDeleteTbls);
		trans.commit();
		
		return true;
	}
	
	public boolean updateRecordCategory(int accountBookId, String oldCategory, String newCategory) {
		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		
		Query query = session.createQuery("UPDATE AccountRecordTbl r SET r.category = ? WHERE r.accountBookId = ? AND r.category = ?");
		query.setParameter(0, newCategory);
		query.setParameter(1, accountBookId);
		query.setParameter(2, oldCategory);
		query.executeUpdate();
		trans.commit();
		return false;
	}
	
	public BoolResult<Map<Integer, Integer>> insertBooks(List<AccountBook> books) {
		// 新账本
		List<AccountBookTbl> newBookTbls = EntitySwitcher
				.toAccountBookTblsForDB(books);
		Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();
		
		if (newBookTbls.size() == 0) {
			BoolResult.True(idMap);
		}

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.save(session, newBookTbls);
		trans.commit();
		
		// 获得保存后的ID与之前的ID映射关系，用于修改账本下的分类和记录
		for (int i = 0; i < books.size(); i++) {
			idMap.put(books.get(i).getAccountBookId(), newBookTbls.get(i).getAccountBookId());
		}
		
		return BoolResult.True(idMap);
	}
	
	public boolean updateBooks(List<AccountBook> books) {
		// 修改账本
		List<AccountBookTbl> updateRecordTbls = EntitySwitcher.toAccountBookTblsForDB(books);
		if (updateRecordTbls.size() == 0) {
			return true;
		}

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.update(session, updateRecordTbls);
		trans.commit();
		return true;
	}
	
	public boolean deleteBooks(List<AccountBook> books, int deleteUserId) {
		// 删除账本
		List<AccountBookTbl> deleteBookTbls = EntitySwitcher
				.toAccountBookTblsForDB(books);
		if (deleteBookTbls.size() == 0) {
			return true;
		}
		
		List<AccountBookDeleteTbl> bookDeleteTbls = EntitySwitcher
				.toAccountBookDeleteTblsForDB(books, deleteUserId);
		
		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		HibernateUtil.delete(session, deleteBookTbls);  // 删除账本
		HibernateUtil.save(session, bookDeleteTbls);    // 保存删除账本记录
		
		// 循环删除账本中的分类和账目
		for (int i = 0; i < deleteBookTbls.size(); i++) {
			int deleteBookId = deleteBookTbls.get(i)
					.getAccountBookId();
			deleteCategoryByAccountBookId(deleteBookId);
			deleteAccountItemByAccountBookId(deleteBookId);
		}
		
		trans.commit();
		
		return true;
	}
	
	public boolean syncData(SyncDataRequestData requestData) {
		try {
			// 新账本
			List<AccountBookTbl> newBookTbls = EntitySwitcher
					.toAccountBookTblsForDB(requestData.getNewBooks());
			// 修改账本
			List<AccountBookTbl> updateBookTbls = EntitySwitcher
					.toAccountBookTblsForDB(requestData.getUpdateBooks());
			// 删除账本
			List<AccountBookTbl> deleteBookTbls = EntitySwitcher
					.toAccountBookTblsForDB(requestData.getDeleteBooks());
			List<AccountBookDeleteTbl> bookDeleteTbls = EntitySwitcher
					.toAccountBookDeleteTblsForDB(requestData.getDeleteBooks(),
							requestData.getUserId());
			// 修改数据库
			Session session = HibernateUtil.getSession();
			Transaction trans = session.beginTransaction();
			
			HibernateUtil.save(session, newBookTbls);		// 新账本
			HibernateUtil.update(session, updateBookTbls);  // 修改账本
			HibernateUtil.delete(session, deleteBookTbls);  // 删除账本
			HibernateUtil.save(session, bookDeleteTbls);    // 保存删除账本记录

			// 循环删除账本中的分类和账目
			for (int i = 0; i < deleteBookTbls.size(); i++) {
				int deleteBookId = deleteBookTbls.get(i)
						.getAccountBookId();
				deleteCategoryByAccountBookId(deleteBookId);
				deleteAccountItemByAccountBookId(deleteBookId);
			}

			trans.commit();

			// 添加书的创建者为书的成员
			for (int i = 0; i < newBookTbls.size(); i++) {
				insertAccountBookMember(newBookTbls.get(i).getCreateUserId(), newBookTbls.get(i)
						.getAccountBookId());
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 检查是否需要更新账本信息
	 * 
	 * @return
	 */
	public boolean checkUpdateBooks(int userId, long lastUpdateTime) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查原表是否有更新
			Query query = session
					.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND r.updateTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				return true;
			}

			// 检查删除表是否有新删除
			query = session
					.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s, AccountBookDeleteTbl t WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND s.id.accountBookId = t.accountBookId AND t.deleteTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 检查是否需要更新分类信息
	 * 
	 * @return
	 */
	public boolean checkUpdateCategories(int userId, long lastUpdateTime) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查原表是否有更新
			Query query = session
					.createQuery("FROM AccountCategoryTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.updateTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				return true;
			}

			// 检查删除表是否有新删除
			query = session
					.createQuery("FROM AccountBookMemberTbl r, AccountCategoryDeleteTbl s WHERE r.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND s.deleteTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			if (query.list().size() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 获得用户的所有账本信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountBookTbl>> getBooksByUserId(int userId) {
		List<AccountBookTbl> accountBookTbls = new ArrayList<AccountBookTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("SELECT r FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId");
			query.setParameter(0, userId);
			accountBookTbls.addAll(query.list());

			return BoolResult.True(accountBookTbls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False();
	}
	
	/**
	 * 获得用户的所有账本分类信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountCategoryTbl>> getCategoriesByUserId(int userId) {
		List<AccountCategoryTbl> categoryTbls = new ArrayList<AccountCategoryTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("SELECT r FROM AccountCategoryTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId");
			query.setParameter(0, userId);
			categoryTbls.addAll(query.list());

			return BoolResult.True(categoryTbls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False();
	}
	
	/**
	 * 获得新添加的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountRecordTbl>> getNewRecordsByUserId(int userId, long lastCreateTime) {
		List<AccountRecordTbl> recordTbls = new ArrayList<AccountRecordTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("SELECT r FROM AccountRecordTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.createTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastCreateTime));
			recordTbls.addAll(query.list());

			return BoolResult.True(recordTbls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False();
	}
	
	/**
	 * 获得更新的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountRecordTbl>> getUpdateRecordsByUserId(int userId, long lastUpdateTime) {
		List<AccountRecordTbl> recordTbls = new ArrayList<AccountRecordTbl>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("SELECT r FROM AccountRecordTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.createTime < ? AND r.updateTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastUpdateTime));
			query.setParameter(2, new Date(lastUpdateTime));
			recordTbls.addAll(query.list());

			return BoolResult.True(recordTbls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False();
	}
	
	/**
	 * 获得更新的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<Integer>> getDeleteRecordIdsByUserId(int userId, long lastDeleteTime) {
		List<Integer> deleteRecordIds = new ArrayList<Integer>();
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("SELECT r.accountRecordId FROM AccountRecordDeleteTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND r.createTime < ? AND r.deleteTime > ?");
			query.setParameter(0, userId);
			query.setParameter(1, new Date(lastDeleteTime));
			query.setParameter(2, new Date(lastDeleteTime));
			deleteRecordIds.addAll(query.list());

			return BoolResult.True(deleteRecordIds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BoolResult.False();
	}

	public boolean deleteCategoryByAccountBookId(int accountBookId) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session
					.createQuery("DELETE FROM AccountCategoryTbl r WHERE r.accountBookId = ?");
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
			Query query = session
					.createQuery("DELETE FROM AccountItemTbl r WHERE r.accountBookId = ?");
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
			Query query = session
					.createQuery("FROM AccountTbl r WHERE r.id.username = ?");
			query.setParameter(0, username);
			if (query.list().size() > 0) {
				return true;
			}
			;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public BoolResult<AccountTbl> getAccount(String username, int accountType) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查是否已经存在用户名
			Query query = session
					.createQuery("FROM AccountTbl r WHERE r.id.username = ? AND r.id.accountType = ?");
			query.setParameter(0, username);
			query.setParameter(1, accountType);

			List<AccountTbl> tbls = query.list();
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

	@SuppressWarnings("unchecked")
	public BoolResult<UserTbl> getUser(int userId) {
		try {
			Session session = HibernateUtil.getSession();
			// 检查是否已经存在用户名
			Query query = session
					.createQuery("FROM UserTbl r WHERE r.userId = ?");
			query.setParameter(0, userId);

			List<UserTbl> tbls = query.list();
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
}