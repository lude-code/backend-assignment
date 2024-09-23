package com.sogetti.leaseservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogetti.leaseservice.car.Car;
import com.sogetti.leaseservice.car.CarRepository;
import com.sogetti.leaseservice.customer.Customer;
import com.sogetti.leaseservice.customer.CustomerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** Class that starts and boots the Spring application */
@SpringBootApplication
public class LeaseDbApplication {

  private static final Logger log = LoggerFactory.getLogger(LeaseDbApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(LeaseDbApplication.class);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public CommandLineRunner demo(
      CustomerRepository customerRepository, CarRepository carRepository) {
    return (args) -> {
    	
    	//TODO remove this for prod
      // save a few customers
      Car car = new Car("Audi", "a", "5", 4, 128, 30000, 90000);
      car = carRepository.save(new Car("Audi", "a", "5", 4, 128, 30000, 90000));
      Customer cust =
          customerRepository.save(
              new Customer(
                  "Jane Doedle",
                  "Hoofdstraat",
                  "1a",
                  "1000 AA",
                  "Adam",
                  "Jan.Doedle@gmail.com",
                  "0611",
                  car));

      ObjectMapper objectMapper = new ObjectMapper();
      System.out.println(objectMapper.writeValueAsString(cust));

      // fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      customerRepository
          .findAll()
          .forEach(
              customer -> {
                log.info(customer.toString());
              });
      log.info("");

      // fetch an individual customer by ID
      Optional<Customer> customer = customerRepository.findById(1L);
      log.info("Customer found with findById(1L):");
      log.info("--------------------------------");
      log.info(customer.toString());
      log.info("");

      // fetch customers by last name
      log.info("Customer found with findByLastName('Jane Doedle'):");
      log.info("--------------------------------------------");
      customerRepository
          .findByName("Bauer")
          .forEach(
              jane -> {
                log.info(jane.toString());
              });
      log.info("");

      // fetch all cars
      log.info("Car found with findAll():");
      log.info("-------------------------------");
      carRepository
          .findAll()
          .forEach(
              cart -> {
                log.info(cart.toString());
              });
      log.info("");

      // fetch an individual car by ID
      Optional<Car> cart = carRepository.findById(1L);
      log.info("Car found with findById(1L):");
      log.info("--------------------------------");
      log.info(cart.toString());
      log.info("");

      // fetch cars by last make model version
      log.info("Car found with findByMakeAndModelAndVersion(audi a 5):");
      log.info("--------------------------------------------");
      carRepository
          .findByMakeAndModelAndVersion("Audi", "a", "5")
          .forEach(
              a5 -> {
                log.info(a5.toString());
              });
      log.info("");
    };
  }
}
