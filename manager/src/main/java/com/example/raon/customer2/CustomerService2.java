package com.example.raon.customer2;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.raon.customer2.Customer2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService2 {

	private final CustomerRepository2 customerRepository2;

	public Page<Customer2> getList(Integer page) {

		Pageable pageable = PageRequest.of(page, 10);
		return this.customerRepository2.findAll(pageable);

	}

	public Customer2 getCustomer2(Long id) {
		Optional<Customer2> customer2 = this.customerRepository2.findById(id);

		return customer2.get();

	}

	public Customer2 getCustomerByCompanyName2(String name2) {
		Customer2 customer2 = customerRepository2.findByCompanyName2(name2);
		return customer2;
	}

	public void enroll(Long id2, String name2, String address2, String phone2) {
		Customer2 c = new Customer2();
		c.setCustomerId2(id2);
		c.setCompanyName2(name2);
		c.setAdress2(address2);
		c.setPhoneNo2(phone2);
		this.customerRepository2.save(c);

	}

	public void delete(Customer2 customer2) {

		this.customerRepository2.delete(customer2);
	}

	public void update(Customer2 customer2, Long id2, String name2, String address2, String phone2) {
		customer2.setCustomerId2(id2);
		customer2.setCompanyName2(name2);
		customer2.setAdress2(address2);
		customer2.setPhoneNo2(phone2);
		this.customerRepository2.save(customer2);
	}

}
