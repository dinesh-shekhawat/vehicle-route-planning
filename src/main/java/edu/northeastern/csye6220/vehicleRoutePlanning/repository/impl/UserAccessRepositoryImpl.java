package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.UserAccessRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserAccessRepositoryImpl implements UserAccessRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccessRepositoryImpl.class);

	@Autowired
	private final HibernateConnectionService hibernateConnectionService;

	private SessionFactory sessionFactory;

	@Autowired
	public UserAccessRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		this.hibernateConnectionService = hibernateConnectionService;
		LOGGER.info("hibernateConnectionService initialized: {}", hibernateConnectionService);
	}

	@PostConstruct
	private void setSessionFactory() {
		sessionFactory = hibernateConnectionService.getSessionFactory();
	}

	@Override
	public UserAccess add(UserAccess userAccess) {
		LOGGER.trace("saving userAccess in database: {}", userAccess);

		Transaction transaction = null;
		
		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			session.persist(userAccess);
			transaction.commit();
			
			session.get(UserAccess.class, userAccess.getId());
		} catch (Exception e) {
			LOGGER.error("exception in save: {}", e.getMessage(), e);
			transaction.rollback();
		}
		
		return userAccess;
	}
	
	@Override
	public UserAccess update(UserAccess userAccess) {
		LOGGER.trace("update userAccess in database: {}", userAccess);

		Transaction transaction = null;
		
		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			session.merge(userAccess);
			transaction.commit();
			
			session.get(UserAccess.class, userAccess.getId());
		} catch (Exception e) {
			LOGGER.error("exception in update: {}", e.getMessage(), e);
			transaction.rollback();
		}
		
		return userAccess;
	}

}
