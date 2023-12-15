package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.AbstractEntity;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.AbstractEntityRepository;

public class AbstractEntityRepositoryImpl<T extends AbstractEntity> implements AbstractEntityRepository<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityRepositoryImpl.class);

	protected SessionFactory sessionFactory;
	private Class<T> clazz;

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
			LOGGER.error("exception in save: {}", e.getMessage(), e);
			transaction.rollback();
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
			LOGGER.error("exception in save: {}", e.getMessage(), e);
			transaction.rollback();
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
			String hql = "FROM " + clazz.getSimpleName() + " WHERE id = :id AND deleted = false";
			Query<T> query = session.createQuery(hql, clazz);
			query.setParameter("id", id);
			t = query.uniqueResult();
		} catch (Exception e) {
			LOGGER.error("Exception in findByIdAndNotDeleted: {}", e.getMessage(), e);
			throw e;
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
			LOGGER.error("exception in save: {}", e.getMessage(), e);
			transaction.rollback();
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
			LOGGER.error("exception in save: {}", e.getMessage(), e);
			transaction.rollback();
			throw e;
		}

		return t;
	}

	@Override
	public SessionFactory getSessionFactory() {
		LOGGER.trace("returning the session factory: {}", sessionFactory);
		return sessionFactory;
	}


}
