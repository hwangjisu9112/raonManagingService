package com.example.raon.customer2;

import java.security.Principal;
import java.time.LocalDate;

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
@RequestMapping("/customer2")
public class CustomerController2 {
	
	private final CustomerService2 customerService2; 
	
	@GetMapping("/list")
	public String CustomerList2(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {page = 0;}

		Page<Customer2> paging = this.customerService2.getList(page);
		model.addAttribute("paging", paging);
		return "customer_list2";
	}

	
	@GetMapping("/enroll")
	public String EnrollCustomer2() {

		return "enroll_customer2";
	}
	
	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id2, 
								@RequestParam String name2,
								@RequestParam LocalDate today,
								@RequestParam String human,
								@RequestParam String business,
								@RequestParam String workingtime,
								@RequestParam String overtime,
								@RequestParam String losetime,
								@RequestParam LocalDate beginning,
								@RequestParam LocalDate end1) {
		customerService2.enroll(id2, name2, today, human, business, workingtime, overtime, losetime,
				beginning, end1);

		return "redirect:/";
	}
	
	@GetMapping("/delete/{id2}")
	public String DeleteCustomer(Principal principal, @PathVariable("id2") Long id2) {
		Customer2 customer2 = this.customerService2.getCustomer2(id2);

		this.customerService2.delete(customer2);
		return "redirect:/customer2/list";
	}
	
	@GetMapping("/update/{id2}")
	public String UpdateCustome2r(Model model, @PathVariable("id2") Long id2) {
		Customer2 customer2 = customerService2.getCustomer2(id2);
		model.addAttribute("customer2", customer2);
		
		
		return "update_customer";
	}
	
	@PostMapping("/update/{id2}") 
	public String UpdateCustomer2(@PathVariable("id2") Long id2, String name2, LocalDate today, String human,
								String business, String workingtime, String overtime, String losetime,
								LocalDate beginning, LocalDate end1,
								@ModelAttribute("customer2") Customer2 customer2) {
		
		customer2.setCustomerId2(id2);
		customer2.setCompanyName2(name2);
		customer2.setToday(today);
		customer2.setHuman(human);
		customer2.setBusiness(business);
		customer2.setWorkingtime(workingtime);
		customer2.setOvertime(overtime);
		customer2.setLosetime(losetime);
		customer2.setBeginning(beginning);
		customer2.setEnd1(end1);

		customerService2.update(customer2, id2, name2, today, human, business, workingtime, overtime,
				losetime, beginning, end1);

		return "redirect:/customer/list2";
	}
	
	

}
