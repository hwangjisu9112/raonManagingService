package com.example.raon.invoice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;
import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final CustomerRepository customerRepository;
	
	private final EmployeeRepository employeeRepository ; 

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		
		return employeeRepository.findAll();
	}

}