package com.sogetti.leaseservice.customer;

import com.sogetti.leaseservice.car.Car;
import com.sogetti.leaseservice.car.CarRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/customer")
public class CustomerControllerImpl implements CustomerController {

  private static final String NOT_FOUND_CUSTOMER = "Not Found Customer";
  private static final String NOT_FOUND_CAR = "Not Found Car";
  private static final String DATABASE_INTEGRITY_EXCEPTION = "Database Integrity Exception";

  private static final Logger log = LoggerFactory.getLogger(CustomerControllerImpl.class);

  private CustomerRepository customerRepository;
  private CarRepository carRepository;

  public CustomerControllerImpl(
      CustomerRepository customerRepository, CarRepository carRepository) {
    super();
    this.customerRepository = customerRepository;
    this.carRepository = carRepository;
  }

  @Override
  @PostMapping("")
  public Customer create(@RequestBody Customer customer) {
    try {
      log.debug("create {}", customer);
      customer.setCar(getCarEntity(customer.getCar()));
      customerRepository.save(customer);
    } catch (DataIntegrityViolationException e) {
      log.warn("create {}", customer, e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATABASE_INTEGRITY_EXCEPTION, e);
    }
    log.debug("created {}", customer);
    return customer;
  }

  @Override
  @PutMapping("{customerName}")
  public Customer update(@PathVariable String customerName, @RequestBody Customer customer) {
    log.debug("update name |{}| with {}", customerName, customer);
    List<Customer> customersFound = customerRepository.findByName(customerName);
    if (customersFound.isEmpty()) {
      log.warn("update name |{}| was not found with {}", customerName, customer);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CUSTOMER);
    }
    customer.setId(customersFound.get(0).getId());
    try {
      customer.setCar(getCarEntity(customer.getCar()));
    } catch (ResponseStatusException e) {
      log.warn("update name |{}|, car was not found with {}", customerName, customer);
      throw e;
    }
    try {
      customerRepository.save(customer);
    } catch (DataIntegrityViolationException e) {
      log.warn("update name |{}|, failed {}", customerName, customer, e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATABASE_INTEGRITY_EXCEPTION, e);
    }
    log.debug("updated name |{}| with {}", customerName, customer);
    return customer;
  }

  @Override
  @GetMapping("{customerName}")
  public Customer get(@PathVariable String customerName) {
    log.debug("get name |{}|", customerName);
    List<Customer> customers;
    customers = customerRepository.findByName(customerName);
    if (!customers.isEmpty()) {
      log.debug("got name |{}| {}", customerName, customers.get(0));
      return customers.get(0);
    } else {
      log.debug("get not found name |{}|", customerName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CUSTOMER);
    }
  }

  @Override
  @GetMapping("")
  public List<Customer> getAll() {
    log.debug("getAll");
    List<Customer> customers = customerRepository.findAll();
    log.debug("getAll {}", customers);
    return customers;
  }

  @Override
  @DeleteMapping("{customerName}")
  public Customer delete(@PathVariable String customerName) {
    log.debug("delete name |{}|", customerName);
    List<Customer> customers;
    customers = customerRepository.findByName(customerName);
    if (!customers.isEmpty()) {
      Customer customer = customers.get(0);
      customerRepository.deleteById(customer.getId());
      log.debug("deleted name |{}| with {}", customerName, customer);
      return customer;
    } else {
      log.warn("delete name |{}| not found", customerName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CUSTOMER);
    }
  }

  private Car getCarEntity(Car car) {
    if (car == null) {
      return null;
    }
    if (car.getId() != null) {
      Optional<Car> carOpt = carRepository.findById(car.getId());
      return carOpt.orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR));
    } else {
      List<Car> cars =
          carRepository.findByMakeAndModelAndVersion(
              car.getMake(), car.getModel(), car.getVersion());
      if (cars.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR);
      } else {
        return cars.get(0);
      }
    }
  }
}
