package com.example.raon.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


//取引先のリポジトリ
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//取引先を全て検索
	Page<Customer> findAll(Pageable pageable);

	//社名で取引先を検索
	Customer findByCompanyName(String companyName);
	
    @Query("SELECT c FROM Customer c")
    List<Customer> getAllCustomers();


}
