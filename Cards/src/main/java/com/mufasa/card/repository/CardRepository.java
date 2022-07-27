package com.mufasa.card.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mufasa.card.entity.Card;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {

	public List<Card> findByCustomerId(int customerId);
}
