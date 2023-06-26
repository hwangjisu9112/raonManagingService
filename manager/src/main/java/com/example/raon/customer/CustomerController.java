package com.example.raon.customer;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
	
	private final CustomerService customerService; 
	
	@GetMapping("/list")
	public String CustomerList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {page = 0;}

		Page<Customer> paging = this.customerService.getList(page);
		model.addAttribute("paging", paging);
		return "customer_list";
	}

	
	@GetMapping("/enroll")
	public String EnrollCustomer() {

		return "enroll_customer";
	}
	
	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id, 
								@RequestParam String name,
								@RequestParam String address,
								@RequestParam String phone) {
		customerService.enroll(id, name, address, phone);

		return "redirect:/";
	}
	
	@GetMapping("/delete/{id}")
	public String DeleteCustomer(Principal principal, @PathVariable("id") Long id) {
		Customer customer = this.customerService.getCustomer(id);

		this.customerService.delete(customer);
		return "redirect:/customer/list";
	}
	
	@GetMapping("/update/{id}")
	public String UpdateCustomer(Model model, @PathVariable("id") Long id) {
		Customer customer = customerService.getCustomer(id);
		model.addAttribute("customer", customer);
		
		
		return "update_customer";
	}
	
	@PostMapping("/update/{id}") 
	public String UpdateCustomer(@PathVariable("id") Long id, String name, String address,
								String phone, 
								@ModelAttribute("customer") Customer customer) {
		
		customer.setCustomerId(id);
		customer.setCompanyName(name);
		customer.setAdress(address);
		customer.setPhoneNo(phone);

		customerService.update(customer, id, name, address, phone);

		return "redirect:/customer/list";
	}
	
	@GetMapping("/subtest")
	public String subtest() {
		
		return "subtest";
	}
	
	@GetMapping("/subtest2")
	public String subtest2() {
		
		return "subtest2";
	}


}
