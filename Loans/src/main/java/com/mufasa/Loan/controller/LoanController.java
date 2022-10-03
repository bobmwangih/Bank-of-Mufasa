package com.mufasa.Loan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mufasa.Loan.config.LoansConfig;
import com.mufasa.Loan.config.Properties;
import com.mufasa.Loan.entity.Customer;
import com.mufasa.Loan.entity.Loan;
import com.mufasa.Loan.repository.LoanRepository;

@RestController
public class LoanController {

	private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
	@Autowired
	private LoanRepository loansRepository;

	@Autowired
	private LoansConfig loansConfig;

	@PostMapping("/myLoans")
	public List<Loan> getLoansDetails(@RequestHeader("mufasa-correlation-id") String correlationid,@RequestBody Customer customer) {
		logger.info("loansDetails() method started");
		System.out.println("Invoking Loans Microservice");
		List<Loan> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		logger.info("loansDetails() method ended");
		if (loans != null) {
			return loans;
		} else {
			return null;
		}

	}

	@GetMapping("/properties")
	public String getLoanProperties() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties props = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		return ow.writeValueAsString(props);
	}
}
