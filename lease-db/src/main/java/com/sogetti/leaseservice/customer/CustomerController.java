package com.sogetti.leaseservice.customer;

import java.util.List;

/**
 * 
 */
public interface CustomerController {

	/**
	 * @return
	 */
	List<Customer> getAll();

	/**
	 * @param customerName
	 * @return
	 */
	Customer get(String customerName);

	/**
	 * @param customer
	 * @return
	 */
	Customer create(Customer customer);

	/**
	 * @param customerName
	 * @param customer
	 * @return
	 */
	Customer update(String customerName, Customer customer);

	/**
	 * @param customerName
	 * @return
	 */
	Customer delete(String customerName);
}
