package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.AbstractEntity;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.AbstractEntityRepository;
import edu.northeastern.csye6220.vehiclerouteplanning.service.AbstractEntityService;

public class AbstractEntityServiceImpl<T extends AbstractEntity> implements AbstractEntityService<T> {

	protected AbstractEntityRepository<T> repository;
	
	protected void register(AbstractEntityRepository<T> repository) {
		this.repository = repository;
	}
	
	@Override
	public T add(T t) {
		return repository.add(t);
	}

	@Override
	public T update(T t) {
		return repository.update(t);
	}

	@Override
	public T findByIdAndNotDeleted(long id) {
		return repository.findByIdAndNotDeleted(id);
	}

	@Override
	public void softDelete(T t) {
		repository.softDelete(t);
	}

	@Override
	public T restoreById(long id) {
		return repository.restoreById(id);
	}

	@Override
	public SessionFactory getSessionFactory() {
		return repository.getSessionFactory();
	}

	@Override
	public List<T> getAllNotDeleted(String user) {
		return repository.getAllNotDeleted(user);
	}
	
	@Override
	public T findByIdAndUserAndNotDeleted(String user, long id) {
		return repository.findByIdAndUserAndNotDeleted(user, id);
	}

}
