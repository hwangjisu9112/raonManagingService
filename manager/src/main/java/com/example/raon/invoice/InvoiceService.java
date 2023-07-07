package com.example.raon.invoice;

import java.util.List;

import com.example.raon.customer.Customer;
import com.example.raon.employee.Employee;

public interface InvoiceService {

	List<Customer> getAllCustomers();

	List<Employee> getAllEmployees();

}