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

//社員ページのコントローラー
public class EmployeeContoller {

	private final EmployeeService employeeService;

	// 社員ページに移動
	@GetMapping("/list")
	public String EmployeeList(Model model, 
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "kw", defaultValue = "") String kw) {

		if (page < 0) {
			page = 0;
		}

		Page<Employee> paging = this.employeeService.getList(page , kw);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		return "employee_list";
	}

	// 社員登録ページに移動
	@GetMapping("/enroll")
	public String EnrollEmployee() {

		return "employee_enroll";
	}

	// 社員登録
	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id, @RequestParam String name, @RequestParam String Ename,
			@RequestParam String Jname, @RequestParam String Pemail, @RequestParam String tel,
			@RequestParam String address, @RequestParam String acc, @RequestParam EmployeeBank bank, // New parameter
																										// for bank
			@RequestParam LocalDate join, @RequestParam LocalDate birth, @RequestParam Integer pay) {

		employeeService.enrollEmp(id, name, Ename, Jname, Pemail, tel, address, acc, bank, join, birth, pay);

		return "redirect:/";
	}

	// 社員情報更新ページに移動
	@GetMapping("/update/{id}")
	public String UpdateEmployee(Model model, @PathVariable("id") Long id) {
		Employee employee = employeeService.getEmployeeID(id);
		model.addAttribute("employee", employee);

		return "employee_update";
	}

	// 社員情報更新
	@PostMapping("/update/{id}")
	public String UpdateEmployee(@PathVariable("id") Long id, @RequestParam String name, @RequestParam String Ename,
			@RequestParam String Jname, @RequestParam String Pemail, @RequestParam String tel,
			@RequestParam String address, @RequestParam String acc, @RequestParam EmployeeBank bank,
			@RequestParam LocalDate join, @RequestParam LocalDate birth, @RequestParam Integer pay,

			@ModelAttribute("employee") Employee employee) {

		employeeService.updateEmp(id, name, Ename, Jname, Pemail, tel, address, acc, bank, join, birth, pay);

		return "redirect:/employee/list";
	}

	// 社員削除
	@GetMapping("/delete/{id}")
	public String DeleteEmployee(Principal principal, @PathVariable("id") Long id) {
		Employee employee = this.employeeService.getEmployeeID(id);

		this.employeeService.delete(employee);
		return "redirect:/employee/list";
	}

}