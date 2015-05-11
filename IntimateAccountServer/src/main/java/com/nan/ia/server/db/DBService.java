package com.nan.ia.server.db;

import java.sql.Timestamp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
