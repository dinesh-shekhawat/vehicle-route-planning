package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.AbstractEntity;

public interface AbstractEntityService<T extends AbstractEntity> {

	T add(T t);
	
	T update(T t);
	
	T findByIdAndNotDeleted(long id);
	
	T findByIdAndUserAndNotDeleted(String user, long id);
	
	void softDelete(T t);
	
	// Undo of softdelete
	T restoreById(long id);
	
	List<T> getAllNotDeleted(String user);
	
	SessionFactory getSessionFactory();
	
}
