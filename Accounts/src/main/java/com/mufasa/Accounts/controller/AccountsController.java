package com.mufasa.Accounts.controller;

import java.util.List;
import java.util.Optional;

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
import com.mufasa.Accounts.config.AccountsConfig;
import com.mufasa.Accounts.config.Properties;
import com.mufasa.Accounts.entity.Account;
import com.mufasa.Accounts.entity.Card;
import com.mufasa.Accounts.entity.Customer;
import com.mufasa.Accounts.entity.CustomerDetails;
import com.mufasa.Accounts.entity.Loans;
import com.mufasa.Accounts.repository.AccountsRepository;
import com.mufasa.Accounts.service.client.CardsFeignClient;
import com.mufasa.Accounts.service.client.LoansFeignClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;

@RestController
public class AccountsController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private AccountsConfig accountsConfig;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;
	
	@Autowired
	private LoansFeignClient loansFeignClient;

	@PostMapping("/myAccount")
	@Timed(value = "getAccountDetails.time",description = "Time taken to return Account Details")
	public Account getAccountDetails(@RequestBody Customer customer) {

		Account accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if (accounts != null) {
			return accounts;
		} else {
			return null;
		}

	}

	@GetMapping("/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		System.out.println("-------------------------"+accountsConfig.getMsg()+"-------------------------");
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
	
	@PostMapping("/customerDetails")
//CircuitBreaker Pattern	
	/*
	 * @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod =
	 * "myCustomerDetailsFallBack" )
	 */
//retry Pattern
	@Retry(name = "retryForCustomerDetails",fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails getCustomerDetails(@RequestHeader("mufasa-correlation-id") String correlationid,@RequestBody Customer customer) {
		logger.info("customerDetails() method started");
		Account account = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Card> cards = cardsFeignClient.getCardsDetails(correlationid,customer);
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationid,customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccount(account);
		customerDetails.setCards(cards);
		customerDetails.setLoans(loans);
		logger.info("customerDetails() method ended");
		return customerDetails;
	}
	
	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("mufasa-correlation-id") String correlationid,Customer customer, Throwable t) {
		Account account = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationid,customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccount(account);
		customerDetails.setLoans(loans);
		return customerDetails;
	}

//Testing the RateLimiter pattern
	@GetMapping("/rateLimiter")
	@RateLimiter(name = "sayHello",fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Rate Limiter test";
	}
	
	private String sayHelloFallback(Throwable t) {
		return "Rate Limiter Fallback.Try again later....  ";
	}
	
	//Testing Kubernetes rollout and rollback - testing Kubernetes loadBalancer na ClusterIp
		@GetMapping("/k8s")
		public String helloK8s() {
			Optional<String> podName = Optional.ofNullable(System.getenv("HOSTNAME"));
			return "Hello K8s Junkie! from: " + podName.get() + " pod";
		}
}
