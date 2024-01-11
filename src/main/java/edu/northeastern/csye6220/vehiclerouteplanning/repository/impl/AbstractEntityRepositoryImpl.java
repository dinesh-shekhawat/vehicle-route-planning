package edu.northeastern.csye6220.vehiclerouteplanning.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.AbstractEntity;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.AbstractEntityRepository;

public class AbstractEntityRepositoryImpl<T extends AbstractEntity> implements AbstractEntityRepository<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityRepositoryImpl.class); // NOSONAR

	protected SessionFactory sessionFactory;
	private Class<T> clazz;
	
	private static final String ENTITY_PARAMETER = "entity";
	
	protected void register(SessionFactory sessionFactory, Class<T> clazz) {
		this.sessionFactory = sessionFactory;
		this.clazz = clazz;
	}

	@Override
	public T add(T t) {
		LOGGER.trace("saving entity t: {}", t);

		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			session.persist(t);
			transaction.commit();

			session.get(clazz, t.getId());
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();	
			}
			
			throw e;
		}

		LOGGER.trace("entity saved in DB: {}", t);
		return t;
	}

	@Override
	public T update(T t) {
		LOGGER.trace("update entity t: {}", t);

		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			session.merge(t);
			transaction.commit();

			session.get(clazz, t.getId());
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();	
			}	
			
			throw e;
		}

		LOGGER.trace("entity updated in DB: {}", t);
		return t;
	}

	@Override
	public T findByIdAndNotDeleted(long id) {
		LOGGER.trace("find entity by id (not deleted): {}", id);

		T t = null;
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM :entity WHERE id = :id AND deleted = false";
			Query<T> query = session.createQuery(hql, clazz);
			query.setParameter(ENTITY_PARAMETER, clazz.getSimpleName());
			query.setParameter("id", id);
			t = query.uniqueResult();
		}

		return t;
	}

	@Override
	public void softDelete(T t) {
		LOGGER.trace("soft delete entity t: {}", t);

		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			t.setDeleted(true);
			session.merge(t);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();	
			}
			throw e;
		}
	}

	@Override
	public T restoreById(long id) {
		LOGGER.trace("restoring entity id: {}", id);

		T t = null;
		Transaction transaction = null;

		try (Session session = sessionFactory.openSession()) {
			transaction = session.getTransaction();
			t = session.get(clazz, id);
			t.setDeleted(false);
			session.merge(t);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();	
			}
			throw e;
		}

		return t;
	}

	@Override
	public SessionFactory getSessionFactory() {
		LOGGER.trace("returning the session factory: {}", sessionFactory);
		return sessionFactory;
	}

	@Override
	public List<T> getAllNotDeleted(String user) {
		LOGGER.trace("get all entities not deleted");

		List<T> entities = null;
		
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM :entity WHERE createdBy = :createdBy AND deleted = false ORDER by createdOn";
			Query<T> query = session.createQuery(hql, clazz);
			query.setParameter(ENTITY_PARAMETER, clazz.getSimpleName());
			query.setParameter("createdBy", user);
			entities = query.list();
		}

		return entities;
	}

	@Override
	public T findByIdAndUserAndNotDeleted(String user, long id) {
		LOGGER.trace("find entity by id, user, and not deleted: {}", id);

	    T t = null;

	    try (Session session = sessionFactory.openSession()) {
	        String hql = "FROM :entity WHERE id = :id AND createdBy = :user AND deleted = false";
	        Query<T> query = session.createQuery(hql, clazz);
			query.setParameter(ENTITY_PARAMETER, clazz.getSimpleName());
	        query.setParameter("id", id);
	        query.setParameter("user", user);

	        t = query.uniqueResult();
	    }

	    return t;
	}

}
