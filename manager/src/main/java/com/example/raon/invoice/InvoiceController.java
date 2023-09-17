package com.example.raon.invoice;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.raon.customer.Customer;
import com.example.raon.employee.Employee;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor

//請求書ページのコントローラー

public class InvoiceController {
	
	//生成子　InvoiceService
	private final InvoiceService invoiceService;


	//請求書閲覧ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/board")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String boardInvoice(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "kw", defaultValue = "") String kw) {
		
		//ページングが0以下にならないように制限
		if (page < 0) {page = 0;}
		
		//リストタイプで請求書情報を出力します
		//page -> ページ処理 , kw -> 検索
		
		Page<Invoice> invoices = this.invoiceService.getList(page, kw);
		
		//paging, kwオブジェクトをmodelに追加
		model.addAttribute("paging", invoices);
		model.addAttribute("kw", kw);
		
		//invoice_board.htmlでレンダリング
		return "invoice_board";
	}

	
	//請求書作成ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/write")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String writeInvoice(Model model) {
		
		//顧客と全職員を連れてきて、それぞれcustomersとemployeesリストに保存
	    List<Customer> customers = invoiceService.getAllCustomers();
	    List<Employee> employees = invoiceService.getAllEmployees();
	    
		//customers, employees, invoiceオブジェクトをmodelに追加
	    model.addAttribute("customers", customers);
	    model.addAttribute("employees", employees);
	    model.addAttribute("invoice", new Invoice());

		//invoice_write.htmlでレンダリング
	    return "invoice_write";
	}

	//請求書作成
	@PostMapping("/write")
	public String writeInvoice(@RequestParam String cpn,
							   @RequestParam String add,
							   @RequestParam String tel,
							   @RequestParam String emp,
	                           @RequestParam String title,
	                           @RequestParam LocalDate date,
	                           @RequestParam Integer w,
	                           @RequestParam Integer ew,
	                           @RequestParam Integer dw,
	                           @RequestParam Integer price,
	                           @RequestParam Integer tax) {

		//enrollメソッドのパラメータ(cpn, add, tel, emp, title, date, w, ew, dw, price, tax)注入
	    invoiceService.write(cpn, add, tel, emp, title, date, w, ew, dw, price, tax);
	    
		//invoice_write.htmlでレンダリング
	    return "redirect:/";
	}

	//請求書閲覧, ADMIN等級ユーザーのみ入場
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String viewInvoice(@PathVariable Long id, Model model) {
		
		//invoiceServiceを通じて指定されたidに対応する請求書を取得します。結果はinvoicesリストに保存されます。
	    List<Invoice> invoices = invoiceService.getInvoicebyId(id);

	    //リストから請求書オブジェクトを取得。リストが空でない場合は、最初の請求書を取得。そうでない場合はnull
	    Invoice invoice = invoices.isEmpty() ? null : invoices.get(0);
	    
		//invoiceオブジェクトをmodelに追加
	    model.addAttribute("invoice", invoice);
	    
		//invoice_view.htmlでレンダリング
	    return "invoice_view";
	}
	
	
	//請求書削除, ADMIN等級ユーザーのみ入場
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String DeleteInvoice(Principal principal, @PathVariable("id") Long id) {
		
		//invoiceServiceを介して、指定されたidに対応する請求書を取得
		Invoice invoice = this.invoiceService.getInvoice(id);

		//deleteメソッドを実行
		this.invoiceService.delete(invoice);
		
		//invoice/board
		return "redirect:/invoice/board";
	}


}
