package com.mufasa.Loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mufasa.Loan.entity.Customer;
import com.mufasa.Loan.entity.Loan;
import com.mufasa.Loan.repository.LoanRepository;

@RestController
public class LoanController {

	@Autowired
	private LoanRepository loansRepository;

	@PostMapping("/myLoans")
	public List<Loan> getLoansDetails(@RequestBody Customer customer) {
		List<Loan> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		if (loans != null) {
			return loans;
		} else {
			return null;
		}

	}
}
