package com.example.raon.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.raon.attendance.Attendance;
import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;
import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {

	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final InvoiceRepository invoiceRepository;

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	public List<Invoice> getList() {
		return this.invoiceRepository.findAll();
	}
	
	public List<Invoice> getInvoicebyId(Long id) {
		return invoiceRepository.findByinvoiceId(id);
	}

	public void write(
			String cpn,
			String add,
			String tel,
			String emp,
			String title, 
			LocalDateTime date, 
			Integer tw, 
			Integer ew, 
			Integer dw,
			Integer price) {

		Invoice i = new Invoice();
		i.setCompanyName(cpn);
		i.setAddress(add);
		i.setTelephoneNumber(tel);
		i.setEmployeeName(emp);
		i.setInvoiceTitle(title);
		i.setIssuedDate(date);
		i.setTotalWorkhour(tw);
		i.setExtraWorkhour(ew);
		i.setDeductionWorkhour(dw);
		i.setUnitPrice(price);
	    this.invoiceRepository.save(i);

	}

}