package com.sogetti.leaseservice.car;

import java.util.List;
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
@RequestMapping("/car")
public class CarControllerImpl implements CarController {

  private static final String DATABASE_INTEGRITY_EXCEPTION = "Database Integrity Exception";
  private static final String NOT_FOUND_CAR = "Not Found Car";

  private static final Logger log = LoggerFactory.getLogger(CarControllerImpl.class);

  private CarRepository carRepository;

  public CarControllerImpl(com.sogetti.leaseservice.car.CarRepository carRepository) {
    super();
    this.carRepository = carRepository;
  }

  @Override
  @PostMapping("")
  public Car create(@RequestBody Car car) {
    try {
      log.debug("create {}", car);
      carRepository.save(car);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATABASE_INTEGRITY_EXCEPTION, e);
    }
    log.debug("created {}", car);
    return car;
  }

  @Override
  @PutMapping("{make}/{model}/{version}")
  public Car update(
      @PathVariable String make,
      @PathVariable String model,
      @PathVariable String version,
      @RequestBody Car car) {
    log.debug(
        "update make |{}|, model |{}|, version |{}| was not found with {}",
        make,
        model,
        version,
        car);
    List<Car> carsFound = carRepository.findByMakeAndModelAndVersion(make, model, version);
    if (carsFound.isEmpty()) {
      log.debug("update make |{}|, model |{}|, version |{}| with {}", make, model, version, car);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR);
    }
    car.setId(carsFound.get(0).getId());
    try {
      carRepository.save(car);
    } catch (DataIntegrityViolationException e) {
      log.debug(
          "update make |{}|, model |{}|, version |{}| failed {}", make, model, version, car, e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATABASE_INTEGRITY_EXCEPTION, e);
    }
    log.debug("update make |{}|, model |{}|, version |{}| with {}", make, model, version, car);
    return car;
  }

  @Override
  @GetMapping("{make}/{model}/{version}")
  public Car get(
      @PathVariable String make, @PathVariable String model, @PathVariable String version) {
    log.debug("get make |{}|, model |{}|, version |{}|", make, model, version);
    List<Car> cars;
    cars = carRepository.findByMakeAndModelAndVersion(make, model, version);
    if (!cars.isEmpty()) {
      log.debug("got make |{}|, model |{}|, version |{}| {}", make, model, version, cars.get(0));
      return cars.get(0);
    } else {
      log.debug("get not fouund make |{}|, model |{}|, version |{}|", make, model, version);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR);
    }
  }

  @Override
  @GetMapping("")
  public List<Car> getAll() {
    log.debug("getAll");
    List<Car> cars = carRepository.findAll();
    log.debug("getAll {}", cars);
    return cars;
  }

  @Override
  @DeleteMapping("{make}/{model}/{version}")
  public Car delete(
      @PathVariable String make, @PathVariable String model, @PathVariable String version) {
    log.debug("delete make |{}|, model |{}|, version |{}|", make, model, version);
    List<Car> cars;
    cars = carRepository.findByMakeAndModelAndVersion(make, model, version);
    if (!cars.isEmpty()) {
      Car car = cars.get(0);
      try {
        carRepository.deleteById(car.getId());
        log.debug("deleted make |{}|, model |{}|, version |{}| with {}", make, model, version, car);
      } catch (DataIntegrityViolationException e) {
        log.debug(
            "delete make |{}|, model |{}|, version |{}| failed with {}",
            make,
            model,
            version,
            car,
            e);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATABASE_INTEGRITY_EXCEPTION, e);
      }
      log.debug("delete make |{}|, model |{}|, version |{}| {}", make, model, version, car);
      return car;
    } else {
      log.debug("delete make |{}|, model |{}|, version |{}| not found", make, model, version);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_CAR);
    }
  }
}
