package com.nan.ia.server.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.nan.ia.common.utils.BoolResult;
import com.nan.ia.server.db.entities.AccountTbl;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration().configure();
        	
            return configuration.buildSessionFactory(
			    new ServiceRegistryBuilder().buildServiceRegistry() );
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
        	ex.printStackTrace();
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session getSession() {
    	Session session = getSessionFactory().getCurrentSession();
    	if (null != session) {
			return session;
		}
//    	
    	session = getSessionFactory().openSession();
    	
    	return session;
    }
    
    public static <T> void saveOrUpdate(Session session, List<T> list) {
    	for (int i = 0; i < list.size(); i++) {
			session.saveOrUpdate(list.get(i));
		}
    }
    
    public static <T> void save(Session session, List<T> list) {
    	for (int i = 0; i < list.size(); i++) {
			session.save(list.get(i));
		}
    }
    
    public static <T> void update(Session session, List<T> list) {
    	for (int i = 0; i < list.size(); i++) {
			session.update(list.get(i));
		}
    }
    
    public static <T> void delete(Session session, List<T> list) {
    	for (int i = 0; i < list.size(); i++) {
			session.delete(list.get(i));
		}
    }
    
    public static abstract class TransactionBoolResultRunable<RESULT> {
		public abstract RESULT run(Session session);
		public BoolResult<RESULT> execute() {
			Session session = HibernateUtil.getSession();
			Transaction trans = session.getTransaction();
			RESULT result = null;
			try {
				trans.begin();
				result = this.run(session);
				trans.commit();
				
				return BoolResult.True(result);
			} catch (Exception e) {
				trans.rollback();
				e.printStackTrace();
			}
			
			return BoolResult.False();
		}
    }
    
    public static abstract class TransactionRunable<RESULT> {
		public abstract RESULT run(Session session);
		public RESULT execute() {
			Session session = HibernateUtil.getSession();
			Transaction trans = session.getTransaction();
			RESULT result = null;
			try {
				trans.begin();
				result = this.run(session);
				trans.commit();
			} catch (Exception e) {
				trans.rollback();
				e.printStackTrace();
			}
			
			return result;
		}
    }
    
    public static void executeTranscation(Runnable runnable) {
    	
    }
}
