package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Customer> getcustomer() {

		// get the current hibernate session
		Session currentsession = sessionFactory.getCurrentSession();

		// create a query
		Query<Customer> theQuery = currentsession.createQuery("from Customer order by lastName", Customer.class);

		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();

		// return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get the current hibernate session
		Session currentsession = sessionFactory.getCurrentSession();

		// save the customer to DB
		currentsession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {

		// get the current hibernate session
		Session currentsession = sessionFactory.getCurrentSession();

		// retrieve the object for DB using prim key
		Customer theCustomer = currentsession.get(Customer.class, theId);

		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		// get the current hibernate session
		Session currentsession = sessionFactory.getCurrentSession();

		// delete object with primary key
		Query theQuery = currentsession.createQuery("delete from Customer where id=:customerId");

		theQuery.setParameter("customerId", theId);

		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomer(String theSearchName) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		Query theQuery = null;

		//
		// only search by name if theSearchName is not empty
		//
		if (theSearchName != null && theSearchName.trim().length() > 0) {
			// search for firstName or lastName ... case insensitive
			theQuery = currentSession.createQuery(
					"from Customer where lower(firstName) like :theName or lower(lastName) like :theName",
					Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		} else {
			// theSearchName is empty ... so just get all customers
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}

		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();

		// return the results
		return customers;
	}

}
