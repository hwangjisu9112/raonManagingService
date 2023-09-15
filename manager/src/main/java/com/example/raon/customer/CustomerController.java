package com.example.raon.customer;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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

//取引先ページのコントローラー
public class CustomerController {
	
	//サービスの生成子
	private final CustomerService customerService; 
	
	
	//取引先のリストの移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String CustomerList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
											@RequestParam(value = "kw", defaultValue = "") String kw) {
		//ページングが0以下にならないように制限
		if (page < 0) {page = 0;}

		//リストタイプで顧客情報を出力します
		//page -> ページ処理 , kw -> 検索
		Page<Customer> paging = this.customerService.getList(page, kw);
		
		//paging, kwオブジェクトをmodelに追加
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		//customer_list.htmlでレンダリング
		return "customer_list";
	}

	//取引先登録ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/enroll")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String EnrollCustomer() {

		//customer_enroll.htmlでレンダリング
		return "customer_enroll";
	}
	
	//取引先登録ページに新しい取引先登録
	@PostMapping("/enroll")
	public String EnrollEmployee(@RequestParam Long id, 
								@RequestParam String name,
								@RequestParam String address,
								@RequestParam String phone) {
		
		//enrollメソッドのパラメータ(id, name, address, phone)注入
		customerService.enroll(id, name, address, phone);

		//customer_enroll.htmlでレンダリング
		return "redirect:/";
	}
	
	//取引先を削除
	//パス変数から固有のIDを取得
	@GetMapping("/delete/{id}")
	public String DeleteCustomer(Principal principal, @PathVariable("id") Long id) {
		
		//与えられたIDに該当する取引先を取得
		Customer customer = this.customerService.getCustomer(id);
		
		//deleteメソッドを実行
		this.customerService.delete(customer);
		
		//メソッドを実行後、　customer/listに移動
		return "redirect:/customer/list";
	}
	
	
	//取引先を更新ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String UpdateCustomer(Model model, @PathVariable("id") Long id) {
		
		//与えられたIDに該当する取引先を取得
		Customer customer = customerService.getCustomer(id);
		
		//customerオブジェクトをmodelに追加
		model.addAttribute("customer", customer);
		
		//customer_update.htmlでレンダリング
		return "customer_update";
	}
	
	//取引先を更新
	@PostMapping("/update/{id}")
	public String UpdateCustomer(@PathVariable("id") Long id, String name, String address,
								String phone, 
								@ModelAttribute("customer") Customer customer) {
		
		//updateメソッドのパラメータ(id, name, address, phone)注入
		customerService.update(customer, id, name, address, phone);
		
		//メソッドを実行後、　customer/listに移動
		return "redirect:/customer/list";
	}
	
}
