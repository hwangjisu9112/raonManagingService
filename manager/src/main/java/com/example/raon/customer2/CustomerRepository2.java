package com.example.raon.customer2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.raon.customer2.Customer2;

public interface CustomerRepository2 extends JpaRepository<Customer2, Long>{

	
	Page<Customer2> findAll(Pageable pageable);
	Customer2 findByCompanyName2(String companyName2);

}
