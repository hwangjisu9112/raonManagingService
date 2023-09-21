package com.example.raon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;


@SpringBootTest
class ManagerApplicationTests {
	
	 @Autowired
	    private CustomerRepository customerRepository;   
   
//	   @Test
//	    void testJpa() {        
//	        Customer c1 = new Customer();
//	        c1.setAddress("sbb가 무엇인가요?");
//	        c1.setCompanyName("sbb에 대해서 알고 싶습니다.");
//	        c1.setCustomerId();
//	        c1.setPhoneNo();
//	        this.customerRepository.save(c1);  // 첫번째 질문 저장
//
//	        

	 
	 
}


