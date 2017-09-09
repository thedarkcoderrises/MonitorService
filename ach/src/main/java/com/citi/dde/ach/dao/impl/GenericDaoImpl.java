package com.citi.dde.ach.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import com.citi.dde.ach.dao.GenericDao;
import com.citi.dde.ach.entity.JobWatcherVO;

public class GenericDaoImpl<T> implements GenericDao<T>{
    @Autowired
    private SessionFactory sessionFactory;
    

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
	

    @Override
    public List<T> findByPaging(String string,Integer page, Integer rows,String sort,String order,String clause) {
    	Session session = sessionFactory.getCurrentSession();
    	int index;
    	String qry = null;
        
		Class clazz;
		try {
			clazz = Class.forName(string);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Class not found.");
		}
		
		if((clause != null) && !clause.equals(""))	
		{
			if(clause.contains("#"))
			{
				clause = clause.replaceAll("#"," and ");
			}
			qry=clause;
		}
    	Criteria criteria ;
    	if(order.equalsIgnoreCase("asc"))
    	{
    		
    			if(qry!=null)
    			{
    				if(sort != null)
    				{
    					criteria = session.createCriteria(clazz).addOrder(Order.asc(sort)).add(Restrictions.sqlRestriction(qry));
    				}
    				else
    				{
    					criteria = session.createCriteria(clazz).add(Restrictions.sqlRestriction(qry));
    				}
    			}
    			else
    			{
    				if(sort != null)
    				{
    					criteria = session.createCriteria(clazz).addOrder(Order.asc(sort));
    				}
    				else
    				{
    					criteria = session.createCriteria(clazz);
    				}
    			}
    	}
    	else
    	{	
    		if(qry!=null)
			{
				if(sort != null)
				{
					criteria = session.createCriteria(clazz).addOrder(Order.desc(sort)).add(Restrictions.sqlRestriction(qry));
				}
				else
				{
					criteria = session.createCriteria(clazz).add(Restrictions.sqlRestriction(qry));
				}
			}
			else
			{
				if(sort != null)
				{
					criteria = session.createCriteria(clazz).addOrder(Order.desc(sort));
				}
				else
				{
					criteria = session.createCriteria(clazz);
				}
			}
    	}
    	if(null!=page && null!= rows){
    		criteria.setFirstResult((page-1)*rows);
        	criteria.setMaxResults(rows);
        }
    	//criteria.setMaxResults(1000);
    	return criteria.list();
    }
    
    
    @Override
    public List<T> getAllDetails(Class<T> clazz) {
    	Session session = sessionFactory.getCurrentSession();
		Criteria criteria ;
		criteria = session.createCriteria(clazz);
		return criteria.list();
    }
    
}
