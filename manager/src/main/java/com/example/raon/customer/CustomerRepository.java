package com.example.raon.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//取引先のリポジトリ
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//
	Page<Customer> findAll(Pageable pageable);

	//
	Customer findByCompanyName(String companyName);

}
