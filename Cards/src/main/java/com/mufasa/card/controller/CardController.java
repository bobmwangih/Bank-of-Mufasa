package com.mufasa.card.controller;

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
import com.mufasa.card.config.CardsConfig;
import com.mufasa.card.config.Properties;
import com.mufasa.card.entity.Card;
import com.mufasa.card.entity.Customer;
import com.mufasa.card.repository.CardRepository;

@RestController
public class CardController {

	private static final Logger logger = LoggerFactory.getLogger(CardController.class);
	@Autowired
	private CardRepository cardsRepository;

	@Autowired
	private CardsConfig cardsConfig;

	@PostMapping("/myCards")
	public List<Card> getCardDetails(@RequestHeader("mufasa-correlation-id") String correlationid,@RequestBody Customer customer) {
		logger.info("getCardDetails() method started");
		List<Card> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		logger.info("getCardDetails() method ended");
		if (cards != null) {
			return cards;
		} else {
			return null;
		}

	}

	@GetMapping("/properties")
	public String getConfigurations() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties props = new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(),
				cardsConfig.getMailDetails(), cardsConfig.getActiveBranches());
		return ow.writeValueAsString(props);
	}

}
