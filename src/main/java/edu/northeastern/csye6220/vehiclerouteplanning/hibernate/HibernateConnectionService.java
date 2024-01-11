package edu.northeastern.csye6220.vehiclerouteplanning.hibernate;

import org.hibernate.SessionFactory;

public interface HibernateConnectionService {
	
	SessionFactory getSessionFactory();
}
