package com.nan.ia.server.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

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
//    	Session session = getSessionFactory().getCurrentSession();
//    	if (null != session) {
//			return session;
//		}
//    	
    	Session session = getSessionFactory().openSession();
    	
    	return session;
    }
}
