package edu.northeastern.csye6220.vehiclerouteplanning.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Location;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.UserAccess;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.Vehicle;
import edu.northeastern.csye6220.vehiclerouteplanning.properties.HibernateProperties;
import jakarta.annotation.PostConstruct;

@Service
public class HibernateConnectionServiceImpl implements HibernateConnectionService {

	private static final String DRIVER_CLASS = "hibernate.connection.driver_class";
	private static final  String URL = "hibernate.connection.url";
	private static final String USERNAME = "hibernate.connection.username";
	private static final String PASSWORD = "hibernate.connection.password";

	private static final String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	private static final String DIALECT = "hibernate.dialect";
	private static final String STORAGE_ENGINE = "hibernate.dialect.storage_engine";
	private static final String SHOW_SQL = "hibernate.show-sql";

	private static final String JDBC_TEMPLATE = "jdbc:mysql://%s:%s/%s";
	private static final String ENTITY_PACKAGE = "edu.northeastern.csye6220.vehiclerouteplanning.entities";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConnectionServiceImpl.class);
	
	private final HibernateProperties hibernateProperties;
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public HibernateConnectionServiceImpl(HibernateProperties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
		LOGGER.info("loaded hibernateProperties: {}", hibernateProperties);
	}
	
	@PostConstruct
	private void buildSessionFactory() {
		LOGGER.info("building session factory with hibernateProperties: {}", hibernateProperties);
		
		Map<String, Object> settings = new HashMap<>();
		
		settings.put(DRIVER_CLASS, hibernateProperties.getDriverClass());
        settings.put(URL, String.format(
        		JDBC_TEMPLATE,
                hibernateProperties.getHost(),
                hibernateProperties.getPort(),
                hibernateProperties.getDatabase()));
        settings.put(USERNAME, hibernateProperties.getUser());
        settings.put(PASSWORD, hibernateProperties.getPassword());

        settings.put(HBM2DDL_AUTO, hibernateProperties.getHbm2ddlAuto());
        settings.put(DIALECT, hibernateProperties.getDialect());
        settings.put(STORAGE_ENGINE, hibernateProperties.getStorageEngine());
        settings.put(SHOW_SQL, String.valueOf(hibernateProperties.isShowSql()));

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addPackage(ENTITY_PACKAGE);
        metadataSources.addAnnotatedClasses(
        		UserAccess.class,
        		User.class,
        		Otp.class,
        		Vehicle.class,
        		Location.class
        );

        Metadata metadata = metadataSources.buildMetadata();
        this.sessionFactory = metadata.getSessionFactoryBuilder().build();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
