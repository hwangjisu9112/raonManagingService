package com.example.raon.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Page<Customer> getList(Integer page) {

		Pageable pageable = PageRequest.of(page, 10);
		return this.customerRepository.findAll(pageable);

	}

	public Customer getCustomer(Long id) {
		Optional<Customer> customer = this.customerRepository.findById(id);

		return customer.get();

	}

	public Customer getCustomerByCompanyName(String name) {
		Customer customer = customerRepository.findByCompanyName(name);
		return customer;
	}

	public void enroll(Long id, String name, String address, String phone) {
		Customer c = new Customer();
		c.setCustomerId(id);
		c.setCompanyName(name);
		c.setAdress(address);
		c.setPhoneNo(phone);
		this.customerRepository.save(c);

	}

	public void delete(Customer customer) {

		this.customerRepository.delete(customer);
	}

	public void update(Customer customer, Long id, String name, String address, String phone) {
		customer.setCustomerId(id);
		customer.setCompanyName(name);
		customer.setAdress(address);
		customer.setPhoneNo(phone);
		this.customerRepository.save(customer);
	}

}
