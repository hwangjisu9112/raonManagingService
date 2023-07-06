package com.example.raon.invoice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final CustomerRepository customerRepository;

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

}