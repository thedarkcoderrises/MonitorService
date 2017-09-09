package com.citi.dde.ach.dao;

import java.util.List;

import org.hibernate.Session;

public interface GenericDao<T> {

	Session getCurrentSession();

	List<T> findByPaging(String table, Integer page, Integer rows, String sort, String order, String clause);
	
	List<T> getAllDetails(Class<T> clazz);

}
