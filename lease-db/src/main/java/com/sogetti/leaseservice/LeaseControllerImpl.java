package com.sogetti.leaseservice;

import com.sogetti.leaseservice.car.Car;
import com.sogetti.leaseservice.car.CarRepository;
import com.sogetti.leaseservice.customer.Customer;
import com.sogetti.leaseservice.customer.CustomerRepository;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/calculate")
public class LeaseControllerImpl implements LeaseController {

  private static final String CUSTOMER_DOES_NOT_HAVE_A_CAR = "Customer does not have a car";
  private static final String NOT_FOUND_CUSTOMER = "Not Found Customer";
  private static final String NOT_FOUND_CAR = "Not Found Car";

  private static final Logger log = LoggerFactory.getLogger(LeaseControllerImpl.class);

  private CustomerRepository customerRepository;
  private CarRepository carRepository;
  private RestTemplate restTemplate;

  @Value("${calculator.url}")
  private String url;

  public LeaseControllerImpl(
      CustomerRepository customerRepository,
      CarRepository carRepository,
      RestTemplate restTemplate) {
    super();
    this.customerRepository = customerRepository;
    this.carRepository = carRepository;
    this.restTemplate = restTemplate;
  }

  @Override
  @GetMapping("car/{make}/{model}/{version}")
  public String getByCar(
      @PathVariable String make,
      @PathVariable String model,
      @PathVariable String version,
      @RequestParam(value = "mileage") double mileage,
      @RequestParam(value = "duration") double duration,
      @RequestParam(value = "interest") double interest) {
    log.debug(
        "calculate make |{}|, model |{}|, version |{}| , mileage |{}|, duration |{}|, interest |{}|",
        make,
        model,
        version,
        mileage,
        duration,
        interest);
    List<Car> cars = carRepository.findByMakeAndModelAndVersion(make, model, version);
    if (cars.isEmpty()) {
      log.warn(
          "calculate did not find car for make |{}|, model |{}|, version |{}|",
          make,
          model,
          version);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR);
    }
    double price = cars.get(0).getNetPrice();
    if (price == 0) {
      log.warn("calculate failed for price off car was 0 for car {}", cars.get(0));
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Car Price was 0");
    }
    // Perform the lookup towards lease calculator service
    log.debug("calculate for car {}", cars.get(0));
    return calculateService(mileage, duration, interest, price);
  }

  @Override
  @GetMapping("customer/{customerName}")
  public String getByCustomer(
      @PathVariable String customerName,
      @RequestParam(value = "mileage") double mileage,
      @RequestParam(value = "duration") double duration,
      @RequestParam(value = "interest") double interest) {
    System.out.println("HELLLOOOOOO");
    log.debug(
        "calculate name |{}|, mileage |{}|, duration |{}|, interest |{}|",
        customerName,
        mileage,
        duration,
        interest);
    List<Customer> customers = customerRepository.findByName(customerName);
    if (customers.isEmpty()) {
      log.warn("calculate did not find customer for name |{}|", customerName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CUSTOMER);
    }
    Customer customer = customers.get(0);
    if (customer.getCar() == null) {
      log.warn("calculate did not find car for customer {}", customer);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, CUSTOMER_DOES_NOT_HAVE_A_CAR);
    }
    double price = customer.getCar().getNetPrice();
    if (price == 0) {
      log.warn("calculate failed for price off car was 0 for customer {}", customer);
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Car Price was 0");
    }
    log.debug("calculate for customer {}", customer);
    return calculateService(mileage, duration, interest, price);
  }

  private String calculateService(double mileage, double duration, double interest, double price) {
    UriComponents builder =
        UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("mileage", Double.toString(mileage))
            .queryParam("duration", Double.toString(duration))
            .queryParam("interest", Double.toString(interest))
            .queryParam("price", price)
            .build();
    ResponseEntity<Double> response =
        restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Double.class);
    Double rate = response.getBody();
    log.debug("calculated rate {}", rate);
    DecimalFormat dec = new DecimalFormat("0.00");
    dec.setRoundingMode(RoundingMode.HALF_UP);
    String rounded = dec.format(rate);
    log.debug("rounded rate {}", rounded);
    return rounded;
  }
}
