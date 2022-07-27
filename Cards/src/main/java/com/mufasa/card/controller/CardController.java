package com.mufasa.card.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mufasa.card.entity.Card;
import com.mufasa.card.entity.Customer;
import com.mufasa.card.repository.CardRepository;

@RestController
public class CardController {

	@Autowired
	private CardRepository cardsRepository;

	@PostMapping("/myCards")
	public List<Card> getCardDetails(@RequestBody Customer customer) {
		List<Card> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		if (cards != null) {
			return cards;
		} else {
			return null;
		}

	}
}
