package com.example.raon.bill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.raon.bill.Customer2;


//請求書のレポジトリ
public interface CustomerRepository2 extends JpaRepository<Customer2, Long>{

	//請求書を全て検索
	Page<Customer2> findAll(Pageable pageable);
	
	//社名で請求書を検索
	Customer2 findByCompanyName2(String companyName2);

}
