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
import com.nan.ia.server.db.HibernateUtil.TransactionRunable;
import com.nan.ia.server.db.HibernateUtil.TransactionBoolResultRunable;

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
			Transaction trans = HibernateUtil.getSession().beginTransaction();
			Query query = HibernateUtil.getSession().createQuery(hqlString);
			trans.commit();
			
			return ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public boolean registerUser(final String username, final String password,
			final int accountType) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				UserTbl userTbl = new UserTbl();
				userTbl.setUserId(0);
				// 默认信息
				userTbl.setNickname(username);
				userTbl.setAvatar("http://p1.gexing.com/touxiang/2011-6/629344710201181462.jpg");
				userTbl.setDescription("很懒哦，什么都没有留下~");
				session.save(userTbl);
				// 昵称填入id
				userTbl.setNickname("用户" + userTbl.getUserId());
				session.save(userTbl);

				// 创建登录账户
				AccountTbl AccountTbl = new AccountTbl();
				AccountTblId AccountTblId = new AccountTblId();
				AccountTblId.setUsername(username);
				AccountTblId.setAccountType(accountType);
				AccountTbl.setId(AccountTblId);
				AccountTbl.setUserId(userTbl.getUserId());
				AccountTbl.setPassword(password);
				session.save(AccountTbl);

				return true;
			}
			
		}.execute();
	}

	public boolean insertCategories(List<AccountCategory> categories) {
		final List<AccountCategoryTbl> newCategoryTbls = EntitySwitcher.toCategoryTblsForDB(categories);
		if (newCategoryTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				HibernateUtil.save(session, newCategoryTbls);
				return true;
			}
			
		}.execute();
	}
	
	public boolean deleteCategories(final List<AccountCategory> deletes, final int deleteUserId) {
		// 构建删除分类列表
		final List<AccountCategoryTbl> deleteCategoryTbls = EntitySwitcher.toCategoryTblsForDB(deletes);
		if (deleteCategoryTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 需要添加到分类删除表中
				List<AccountCategoryDeleteTbl> categoryDeleteTbls = EntitySwitcher.toCategoryDeleteTblsForDB(deletes, deleteUserId);
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
				return true;
			}
			
		}.execute();
	}
	
	public boolean insertRecords(List<AccountRecord> records) {
		// 新记录
		final List<AccountRecordTbl> newRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (newRecordTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				HibernateUtil.save(session, newRecordTbls);
				return true;
			}
			
		}.execute();
	}
	
	public boolean updateRecords(List<AccountRecord> records) {
		final List<AccountRecordTbl> updateRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (updateRecordTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				HibernateUtil.update(session, updateRecordTbls);
				return true;
			}
			
		}.execute();
	}
	
	public boolean deleteRecords(final List<AccountRecord> records, final int deleteUserId) {
		// 构建删除记录数据
		final List<AccountRecordTbl> deleteRecordTbls = EntitySwitcher.toRecordTblsForDB(records);
		if (deleteRecordTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 需要添加到记录删除表中
				List<AccountRecordDeleteTbl> recordDeleteTbls = EntitySwitcher.toRecordDeleteTblsForDB(records, deleteUserId);
				// 删除记录数据
				HibernateUtil.delete(session, deleteRecordTbls);
				// 添加记录删除表中的数据
				HibernateUtil.save(session, recordDeleteTbls);
				
				return true;
			}
			
		}.execute();
	}
	
	public boolean updateRecordCategory(final int accountBookId, final String oldCategory, final String newCategory) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				Query query = session.createQuery("UPDATE AccountRecordTbl r SET r.category = ? WHERE r.accountBookId = ? AND r.category = ?");
				query.setParameter(0, newCategory);
				query.setParameter(1, accountBookId);
				query.setParameter(2, oldCategory);
				query.executeUpdate();
				return false;
			}
			
		}.execute();
	}
	
	public BoolResult<Map<Integer, Integer>> insertBooks(final List<AccountBook> books) {
		return new TransactionBoolResultRunable<Map<Integer, Integer>>() {

			@Override
			public Map<Integer, Integer> run(Session session) {
				// 新账本
				List<AccountBookTbl> newBookTbls = EntitySwitcher
						.toAccountBookTblsForDB(books);
				Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();
				if (newBookTbls.size() == 0) {
					return idMap;
				}

				HibernateUtil.save(session, newBookTbls);
				// 获得保存后的ID与之前的ID映射关系，用于修改账本下的分类和记录
				for (int i = 0; i < books.size(); i++) {
					idMap.put(books.get(i).getAccountBookId(), newBookTbls.get(i).getAccountBookId());
				}
				
				return idMap;
			}
			
		}.execute();
	}
	
	public boolean updateBooks(List<AccountBook> books) {
		// 修改账本
		final List<AccountBookTbl> updateRecordTbls = EntitySwitcher.toAccountBookTblsForDB(books);
		if (updateRecordTbls.size() == 0) {
			return true;
		}
		
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				HibernateUtil.update(session, updateRecordTbls);
				return true;
			}
			
		}.execute();
	}
	
	public boolean deleteBooks(final List<AccountBook> books, final int deleteUserId) {
		// 删除账本
		final List<AccountBookTbl> deleteBookTbls = EntitySwitcher
				.toAccountBookTblsForDB(books);
		if (deleteBookTbls.size() == 0) {
			return true;
		}
		
		final List<AccountBookDeleteTbl> bookDeleteTbls = EntitySwitcher
				.toAccountBookDeleteTblsForDB(books, deleteUserId);
		
		Boolean result = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				HibernateUtil.delete(session, deleteBookTbls);  // 删除账本
				HibernateUtil.save(session, bookDeleteTbls);    // 保存删除账本记录
				return true;
			}
			
		}.execute();
		
		if (!result) {
			return false;
		}
		
		// 循环删除账本中的分类和账目
		for (int i = 0; i < deleteBookTbls.size(); i++) {
			int deleteBookId = deleteBookTbls.get(i).getAccountBookId();
			deleteCategoryByAccountBookId(deleteBookId);
			deleteAccountItemByAccountBookId(deleteBookId);
		}
		
		return true;
	}
	
	public List<AccountBookMemberTbl> getBookMembers(final int accountBookId) {
		return new TransactionRunable<List<AccountBookMemberTbl>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<AccountBookMemberTbl> run(Session session) {
				// 检查成员表是否有新成员
				Query query = session
						.createQuery("FROM AccountBookMemberTbl r WHERE r.id.accountBookId = ?");
				query.setParameter(0, accountBookId);
				return query.list();
			}
			
		}.execute();
	}

	/**
	 * 检查是否需要更新账本信息
	 * 
	 * @return
	 */
	public boolean checkUpdateBooks(final int userId, final long lastUpdateTime) {
		// 检查原表是否有更新
		Boolean needUpdateBooks = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				Query query = session
						.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND r.updateTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastUpdateTime));
				return query.list().size() > 0;
			}
			
		}.execute();
		
		if (needUpdateBooks) {
			return true;
		}
		
		needUpdateBooks = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 检查删除表是否有新删除
				Query query = session
						.createQuery("FROM AccountBookTbl r, AccountBookMemberTbl s, AccountBookDeleteTbl t WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND s.id.accountBookId = t.accountBookId AND t.deleteTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastUpdateTime));
				return query.list().size() > 0;
			}
			
		}.execute();
		
		if (needUpdateBooks) {
			return true;
		}
		
		needUpdateBooks = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 检查成员表是否有新成员
				Query query = session.createQuery("FROM AccountBookMemberTbl r WHERE r.createTime > ? AND r.id.accountBookId IN( SELECT s.id.accountBookId FROM AccountBookMemberTbl s WHERE s.id.memberUserId = ? )");
				query.setParameter(0, new Date(lastUpdateTime));
				query.setParameter(1, userId);
				return query.list().size() > 0;
			}
			
		}.execute();
		
		if (needUpdateBooks) {
			return true;
		}

		return false;
	}
	
	/**
	 * 检查是否需要更新分类信息
	 * 
	 * @return
	 */
	public boolean checkUpdateCategories(final int userId, final long lastUpdateTime) {
		Boolean needUpdateCategories = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 检查原表是否有更新
				Query query = session
						.createQuery("FROM AccountCategoryTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.updateTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastUpdateTime));
				return query.list().size() > 0;
			}
			
		}.execute();
		
		if (needUpdateCategories) {
			return true;
		}
		
		needUpdateCategories = new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 检查删除表是否有新删除
				Query query = session
						.createQuery("FROM AccountBookMemberTbl r, AccountCategoryDeleteTbl s WHERE r.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND s.deleteTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastUpdateTime));
				return query.list().size() > 0;
			}
			
		}.execute();
		
		if (needUpdateCategories) {
			return true;
		}

		return false;
	}

	/**
	 * 获得用户的所有账本信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountBookTbl>> getBooksByUserId(final int userId) {
		return new TransactionBoolResultRunable<List<AccountBookTbl>>() {

			@Override
			public List<AccountBookTbl> run(Session session) {
				final List<AccountBookTbl> accountBookTbls = new ArrayList<AccountBookTbl>();
				Query query = session
						.createQuery("SELECT r FROM AccountBookTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId");
				query.setParameter(0, userId);
				accountBookTbls.addAll(query.list());
				
				return accountBookTbls;
			}
			
		}.execute();
	}
	
	/**
	 * 获得用户的所有账本分类信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountCategoryTbl>> getCategoriesByUserId(final int userId) {
		return new TransactionBoolResultRunable<List<AccountCategoryTbl>>() {

			@Override
			public List<AccountCategoryTbl> run(Session session) {
				List<AccountCategoryTbl> categoryTbls = new ArrayList<AccountCategoryTbl>();
				Query query = session
						.createQuery("SELECT r FROM AccountCategoryTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId");
				query.setParameter(0, userId);
				categoryTbls.addAll(query.list());
				
				return categoryTbls;
			}
			
		}.execute();
	}
	
	/**
	 * 获得新添加的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountRecordTbl>> getNewRecordsByUserId(final int userId, final long lastCreateTime) {
		return new TransactionBoolResultRunable<List<AccountRecordTbl>>() {

			@Override
			public List<AccountRecordTbl> run(Session session) {
				List<AccountRecordTbl> recordTbls = new ArrayList<AccountRecordTbl>();
				Query query = session
						.createQuery("SELECT r FROM AccountRecordTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.createTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastCreateTime));
				recordTbls.addAll(query.list());
				
				return recordTbls;
			}
			
		}.execute();
	}
	
	/**
	 * 获得更新的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<AccountRecordTbl>> getUpdateRecordsByUserId(final int userId, final long lastUpdateTime) {
		return new TransactionBoolResultRunable<List<AccountRecordTbl>>() {

			@Override
			public List<AccountRecordTbl> run(Session session) {
				List<AccountRecordTbl> recordTbls = new ArrayList<AccountRecordTbl>();
				Query query = session
						.createQuery("SELECT r FROM AccountRecordTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.id.accountBookId = s.id.accountBookId AND r.createTime < ? AND r.updateTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastUpdateTime));
				query.setParameter(2, new Date(lastUpdateTime));
				recordTbls.addAll(query.list());
				
				return recordTbls;
			}
			
		}.execute();
	}
	
	/**
	 * 获得更新的的账本记录信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BoolResult<List<Integer>> getDeleteRecordIdsByUserId(final int userId, final long lastDeleteTime) {
		return new TransactionBoolResultRunable<List<Integer>>() {

			@Override
			public List<Integer> run(Session session) {
				List<Integer> deleteRecordIds = new ArrayList<Integer>();
				Query query = session
						.createQuery("SELECT r.accountRecordId FROM AccountRecordDeleteTbl r, AccountBookMemberTbl s WHERE s.id.memberUserId = ? AND r.accountBookId = s.id.accountBookId AND r.createTime < ? AND r.deleteTime > ?");
				query.setParameter(0, userId);
				query.setParameter(1, new Date(lastDeleteTime));
				query.setParameter(2, new Date(lastDeleteTime));
				deleteRecordIds.addAll(query.list());
				
				return deleteRecordIds;
			}
			
		}.execute();
	}

	public boolean deleteCategoryByAccountBookId(final int accountBookId) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				Query query = session
						.createQuery("DELETE FROM AccountCategoryTbl r WHERE r.id.accountBookId = ?");
				query.setParameter(0, accountBookId);
				query.executeUpdate();
				
				return true;
			}
			
		}.execute();
	}

	public boolean deleteAccountItemByAccountBookId(final int accountBookId) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				Query query = session
						.createQuery("DELETE FROM AccountRecordTbl r WHERE r.accountBookId = ?");
				query.setParameter(0, accountBookId);
				query.executeUpdate();
				
				return true;
			}
			
		}.execute();
	}

	public boolean insertAccountBookMember(final int userId, final int accountBookId) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				AccountBookMemberTbl tbl = new AccountBookMemberTbl();
				AccountBookMemberTblId tblId = new AccountBookMemberTblId();
				tblId.setMemberUserId(userId);
				tblId.setAccountBookId(accountBookId);
				tbl.setId(tblId);
				session.save(tbl);
				
				return true;
			}
			
		}.execute();
	}

	public boolean existUsername(final String username) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				// 检查是否已经存在用户名
				Query query = session
						.createQuery("FROM AccountTbl r WHERE r.id.username = ?");
				query.setParameter(0, username);
				int size = query.list().size();
				return size > 0;
			}
			
		}.execute();
	}

	@SuppressWarnings("unchecked")
	public BoolResult<AccountTbl> getAccount(final String username) {
		return new TransactionBoolResultRunable<AccountTbl>() {

			@Override
			public AccountTbl run(Session session) {
				// 检查是否已经存在用户名
				Query query = session
						.createQuery("FROM AccountTbl r WHERE r.id.username = ?");
				query.setParameter(0, username);
				List<AccountTbl> tbls = query.list();
				
				if (tbls.size() == 1) {
					return tbls.get(0);
				} else {
					return null;
				}
			}
			
		}.execute();
	}

	public boolean updateLoginState(final int userId, final String token) {
		return new TransactionRunable<Boolean>() {

			@Override
			public Boolean run(Session session) {
				LoginStateTbl tbl = new LoginStateTbl();
				tbl.setUserId(userId);
				tbl.setToken(token);
				session.saveOrUpdate(tbl);
				return true;
			}
			
		}.execute();
	}

	@SuppressWarnings("unchecked")
	public BoolResult<UserTbl> getUser(final int userId) {
		return new TransactionBoolResultRunable<UserTbl>() {

			@Override
			public UserTbl run(Session session) {
				// 检查是否已经存在用户名
				Query query = session
						.createQuery("FROM UserTbl r WHERE r.userId = ?");
				query.setParameter(0, userId);
				List<UserTbl> tbls = query.list();
				
				if (tbls.size() == 1) {
					return tbls.get(0);
				} else {
					return null;
				}
			}
			
		}.execute();
	}
	
	public BoolResult<List<UserTbl>> getUsers(final List<Integer> userIds) {
		return new TransactionBoolResultRunable<List<UserTbl>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<UserTbl> run(Session session) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < userIds.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}

					sb.append(userIds.get(i));
				}

				Query query = session
						.createQuery(String.format("FROM UserTbl r WHERE r.userId IN(%s)", sb.toString()));
				return query.list();
			}
			
		}.execute();
	}
}