package edu.northeastern.csye6220.vehiclerouteplanning.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;
import edu.northeastern.csye6220.vehiclerouteplanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.UserRepository;

@Service
public class UserRepositoryImpl extends AbstractEntityRepositoryImpl<User> implements UserRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccessRepositoryImpl.class);

	@Autowired
	public UserRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), User.class);
	}
	
	@Override
	public User findByEmailIdAndNotDeleted(String email) {
		LOGGER.trace("retrieving user by email: {}", email);
		
		User user = null;
		
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM User WHERE email = :email AND deleted = :deleted";
			Query<User> query = session.createQuery(hql, User.class);
			query.setParameter("email", email);
			query.setParameter("deleted", false);

			user = query.uniqueResult();
		} catch (Exception e) {
			LOGGER.error("Exception in findByEmailIdAndNotDeleted: {}", e.getMessage(), e);
			throw e;
		}
		
		LOGGER.trace("found the user: {}", user);
		return user;
	}
}
