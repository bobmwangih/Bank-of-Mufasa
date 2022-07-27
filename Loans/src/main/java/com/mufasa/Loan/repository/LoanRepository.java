package com.mufasa.Loan.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mufasa.Loan.entity.Loan;

@Repository
public interface LoanRepository  extends CrudRepository<Loan, Long>{

	List<Loan> findByCustomerIdOrderByStartDtDesc(int customerId);
}
