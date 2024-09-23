package com.sogetti.leaseservice;

/**
 * This Rest Interface calculates  
 */
public interface LeaseController {
	
	/**
	 * Calculates the lease rate based on the car
	 * 
	 * @param make of car
	 * @param model of car
	 * @param version of car
	 * @param mileage in km per year
	 * @param duration in months
	 * @param interest yearly interest (percentage x.x%)
	 * @return rate in euro and cent x.xx
	 */
	String getByCar(String make, String model, String version, double mileage, double duration, double interest);

	/**
	 * Calculates the lease rate based on the car that the user leases 
	 * 
	 * @param customerName full customer name
	 * @param mileage in km per year
	 * @param duration in months
	 * @param interest yearly interest (percentage x.x%)
	 * @return rate in euro and cent x.xx
	 */
	String getByCustomer(String customerName, double mileage, double duration, double interest);
}
