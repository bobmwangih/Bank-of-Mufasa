package com.mufasa.Accounts.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDetails {

	private Account account;
	private List<Card> cards;
	private List<Loans> loans;
	
}
