package com.sogetti.leasecalculatorservice;

/**
 * Microservice that calculates the rate of the lease
 */
public interface LeaseCalculatorController {

	/**
	 * Calculates the rate
	 * 
	 * @param mileage in km per year
	 * @param duration in months
	 * @param price in euro
	 * @param interest yearly interest (percentage x.x%)
	 * @return rate in euro
	 */
	double calculate(double mileage, double duration, double price, double interest);
}
