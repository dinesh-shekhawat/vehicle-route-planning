package edu.northeastern.csye6220.vehicleRoutePlanning.hibernate;

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

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.HibernateProperties;
import jakarta.annotation.PostConstruct;

@Service
public class HibernateConnectionServiceImpl implements HibernateConnectionService {

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
        		UserAccess.class
        );

        Metadata metadata = metadataSources.buildMetadata();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        this.sessionFactory = sessionFactory;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
