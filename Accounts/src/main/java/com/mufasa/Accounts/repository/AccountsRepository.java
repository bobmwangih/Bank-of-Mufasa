package com.mufasa.Accounts.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mufasa.Accounts.entity.Account;

@Repository
public interface AccountsRepository extends CrudRepository<Account, Long>{

	Account findByCustomerId(int customerId);
}
