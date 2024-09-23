package com.sogetti.leasecalculatorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class LeaseCalculatorControllerImpl implements LeaseCalculatorController {
	private static final Logger log = LoggerFactory.getLogger(LeaseCalculatorController.class);

	@Override
	@GetMapping("/calculate")
	public double calculate(@RequestParam(value = "mileage") double mileage,
			@RequestParam(value = "duration") double duration, @RequestParam(value = "price") double price,
			@RequestParam(value = "interest") double interest) {
		log.debug("calculate mileage |{}|, duration |{}|, price |{}|, interest |{}|", mileage, duration, price, interest);
		if (price == 0) {
			log.warn("calculate mileage |{}|, duration |{}|, price |{}|, interest |{}| invalid price");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Value");
		}
		double rate = (((mileage / 12) * duration) / price) + (((interest / 100) * price) / 12);
		log.debug("calculated rate ", rate);
		return rate;
	}
}
