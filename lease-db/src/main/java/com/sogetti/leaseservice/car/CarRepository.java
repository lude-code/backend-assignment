package com.sogetti.leaseservice.car;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {

  List<Car> findByMakeAndModelAndVersion(String make, String model, String version);

  @Override
  List<Car> findAll();
}
