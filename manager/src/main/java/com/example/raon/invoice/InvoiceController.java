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
	
	//生成子
	private final InvoiceService invoiceService;


	//請求書閲覧ページに移動
	@GetMapping("/board")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String boardInvoice(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "kw", defaultValue = "") String kw) {
		
		if (page < 0) {page = 0;}
		
		Page<Invoice> invoices = this.invoiceService.getList(page, kw);
		
		model.addAttribute("paging", invoices);
		model.addAttribute("kw", kw);
		return "invoice_board";
	}

	
	//請求書作成ページに移動
	@GetMapping("/write")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String writeInvoice(Model model) {
	    List<Customer> customers = invoiceService.getAllCustomers();
	    List<Employee> employees = invoiceService.getAllEmployees();

	    model.addAttribute("customers", customers);
	    model.addAttribute("employees", employees);
	    model.addAttribute("invoice", new Invoice());

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

	    invoiceService.write(cpn, add, tel, emp, title, date, w, ew, dw, price, tax);

	    return "redirect:/";
	}

	//請求書閲覧
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String viewInvoice(@PathVariable Long id, Model model) {
	    List<Invoice> invoices = invoiceService.getInvoicebyId(id);
	    Invoice invoice = invoices.isEmpty() ? null : invoices.get(0);
	    model.addAttribute("invoice", invoice);
	    return "invoice_view";
	}
	
	
	//請求書削除
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String DeleteInvoice(Principal principal, @PathVariable("id") Long id) {
		
		Invoice invoice = this.invoiceService.getInvoice(id);

		this.invoiceService.delete(invoice);
		return "redirect:/invoice/board";
	}


}
