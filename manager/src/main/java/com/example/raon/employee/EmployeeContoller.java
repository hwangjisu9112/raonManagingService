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
import org.springframework.security.access.prepost.PreAuthorize;


@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")

//社員ページのコントローラー
public class EmployeeContoller {

	private final EmployeeService employeeService;

	// 社員ページに移動, ADMIN等級ユーザーのみ入場 
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String EmployeeList(Model model, 
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "kw", defaultValue = "") String kw) {

		//ページングが0以下にならないように制限
		if (page < 0) {
			page = 0;
		}

		
	    //コードとページに該当する出席データを取得。 このデータはattendancePage変数に割り当てます

		Page<Employee> paging = this.employeeService.getList(page , kw);
		
		//paging, kwオブジェクトをmodelに追加
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		//employee_list.htmlでレンダリング
		return "employee_list";
	}

	// 社員登録ページに移動
	@GetMapping("/enroll")
	public String EnrollEmployee() {

		//employee_enroll.htmlでレンダリング
		return "employee_enroll";
	}

	// 社員登録
	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id, @RequestParam String name, @RequestParam String Ename,
			@RequestParam String Jname, @RequestParam String Pemail, @RequestParam String tel,
			@RequestParam String address, @RequestParam String acc, @RequestParam EmployeeBank bank,
			@RequestParam LocalDate join, @RequestParam LocalDate birth, @RequestParam Integer pay) {

		//enrollEmpメソッドを実行
		employeeService.enrollEmp(id, name, Ename, Jname, Pemail, tel, address, acc, bank, join, birth, pay);

		// リダイレクト -> Main
		return "redirect:/";
	}

	// 社員情報更新ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String UpdateEmployee(Model model, @PathVariable("id") Long id) {
		
		//与えられたコードに該当する職員IDを探します
		Employee employee = employeeService.getEmployeeID(id);
		
		//employeeオブジェクトをmodelに追加
		model.addAttribute("employee", employee);

		//employee_update.htmlでレンダリング
		return "employee_update";
	}

	// 社員情報更新
	@PostMapping("/update/{id}")
	public String UpdateEmployee(@PathVariable("id") Long id, @RequestParam String name, @RequestParam String Ename,
			@RequestParam String Jname, @RequestParam String Pemail, @RequestParam String tel,
			@RequestParam String address, @RequestParam String acc, @RequestParam EmployeeBank bank,
			@RequestParam LocalDate join, @RequestParam LocalDate birth, @RequestParam Integer pay,
			@ModelAttribute("employee") Employee employee) {

		//updateEmpメソッドを実行
		employeeService.updateEmp(id, name, Ename, Jname, Pemail, tel, address, acc, bank, join, birth, pay);

		//リダイレクト -> /employee/list
		return "redirect:/employee/list";
	}

	// 社員削除, ADMIN等級ユーザーのみ入場
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String DeleteEmployee(Principal principal, @PathVariable("id") Long id) {
		
		//与えられたコードに該当する職員IDを探します
		Employee employee = this.employeeService.getEmployeeID(id);

		//deleteメソッドを実行
		this.employeeService.delete(employee);
		
		//リダイレクト -> /employee/list
		return "redirect:/employee/list";
	}

}