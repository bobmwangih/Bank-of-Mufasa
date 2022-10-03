package com.mufasa.Accounts.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mufasa.Accounts.entity.Card;
import com.mufasa.Accounts.entity.Customer;

@FeignClient("cards")
public interface CardsFeignClient {

	@RequestMapping(method = RequestMethod.POST, value ="myCards", consumes = "application/json")
	List<Card> getCardsDetails(@RequestHeader("mufasa-correlation-id") String correlationid,@RequestBody Customer customer);
}
