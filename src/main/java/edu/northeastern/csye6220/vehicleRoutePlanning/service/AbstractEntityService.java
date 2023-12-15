package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import org.hibernate.SessionFactory;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.AbstractEntity;

public interface AbstractEntityService<T extends AbstractEntity> {

	T add(T t);
	
	T update(T t);
	
	T findByIdAndNotDeleted(long id);
	
	void softDelete(T t);
	
	// Undo of softdelete
	T restoreById(long id);
	
	SessionFactory getSessionFactory();
	
}