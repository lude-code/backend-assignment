package com.sogetti.leaseservice.car;

import java.util.List;

/**
 * Rest interface for maintaining car information
 */
public interface CarController {

	/**
	 * Create a new car
	 * 
	 * @param car
	 * @return the created car
	 */
	Car create(Car car);

	/**
	 * Updates the car
	 * 
	 * @param make of car to be updated
	 * @param model of car to be updated
	 * @param version of car to be updated
	 * @param car the new entry to be used
	 * @return the updated car
	 */
	Car update(String make, String model, String version, Car car);

	/**
	 * Get the specified car
	 * 
	 * @param make
	 * @param model
	 * @param version
	 * @return 
	 */
	Car get(String make, String model, String version);

	/**
	 * Get all cars
	 * 
	 * @return 
	 */
	List<Car> getAll();

	/**
	 * Delete the car
	 * 
	 * @param make
	 * @param model
	 * @param version
	 * @return car that was deleted
	 */
	Car delete(String make, String model, String version);
}
