package com.example.raon.employee;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeContoller {

	private final EmployeeService employeeService;

	@GetMapping("/list")
	public String EmployeeList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {
			page = 0;
		}

		Page<Employee> paging = this.employeeService.getList(page);
		model.addAttribute("paging", paging);
		return "employee_list";
	}

	@GetMapping("/enroll")
	public String EnrollEmployee() {

		return "enroll_employee";
	}

	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id, 
            @RequestParam String name, 
            @RequestParam String Ename, 
            @RequestParam String Jname, 
            @RequestParam String Eemail, 
            @RequestParam String Pemail, 
            @RequestParam String tel, 
            @RequestParam String address, 
            @RequestParam String acc, 
            @RequestParam EmployeeBank bank, // New parameter for bank
            @RequestParam LocalDate join,
            @RequestParam LocalDate birth,
            @RequestParam Integer pay) {

employeeService.enrollEmp(id, name, Ename, Jname, Eemail, Pemail, tel, address, acc, bank, join, birth, pay);


return "redirect:/";
}
	@GetMapping("/update/{id}")
	public String UpdateEmployee(Model model, @PathVariable("id") Long id) {
		Employee employee = employeeService.getEmployeeID(id);
		model.addAttribute("employee", employee);

		return "update_employee";
	}
	
	

	@PostMapping("/update/{id}")
	public String UpdateEmployee(@PathVariable("id") Long id, 
            @RequestParam String name, 
            @RequestParam String Ename, 
            @RequestParam String Jname, 
            @RequestParam String Eemail, 
            @RequestParam String Pemail, 
            @RequestParam String tel, 
            @RequestParam String address, 
            @RequestParam String acc, 
            @RequestParam EmployeeBank bank,
            @RequestParam LocalDate join,
            @RequestParam LocalDate birth,
            @RequestParam Integer pay,
			
			@ModelAttribute("employee") Employee employee) {

		employeeService.updateEmp(id, name, Ename, Jname, Eemail, Pemail, tel, address, acc, bank, join, birth, pay);

		
		return "redirect:/employee/list";
	}

	@GetMapping("/delete/{id}")
	public String DeleteEmployee(Principal principal, @PathVariable("id") Long id) {
		Employee employee = this.employeeService.getEmployeeID(id);

		this.employeeService.delete(employee);
		return "redirect:/employee/list";
	}

//    @PostMapping("/checkin/{id}")
//    public String checkIn(@PathVariable Long id) {
//        employeeService.checkIn(id);
//        return "redirect:/employee/list"; 
//    }
//
//    @PostMapping("/checkout/{id}")
//    public String checkOut(@PathVariable Long id) {
//        employeeService.checkOut(id);
//        return "redirect:/employee/list";
//    }

}