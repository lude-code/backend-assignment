package com.sogetti.leaseservice.customer;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

  List<Customer> findByName(String name);

  @Override
  List<Customer> findAll();
}
