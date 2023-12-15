package edu.northeastern.csye6220.vehicleRoutePlanning.hibernate;

import org.hibernate.SessionFactory;

public interface HibernateConnectionService {
	
	String DRIVER_CLASS = "hibernate.connection.driver_class";
    String URL = "hibernate.connection.url";
    String USERNAME = "hibernate.connection.username";
    String PASSWORD = "hibernate.connection.password";

    String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    String DIALECT = "hibernate.dialect";
    String STORAGE_ENGINE = "hibernate.dialect.storage_engine";
    String SHOW_SQL = "hibernate.show-sql";

    String JDBC_TEMPLATE = "jdbc:mysql://%s:%s/%s";
	String ENTITY_PACKAGE = "edu.northeastern.csye6220.vehicleRoutePlanning.entities";

	SessionFactory getSessionFactory();
}
