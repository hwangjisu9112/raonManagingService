package com.example.raon.invoice;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
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
	private final InvoiceRepository invoiceRepository ;


	//請求書閲覧ページに移動
	@GetMapping("/board")
	public String BoardInvoice(Model model) {
	
		List<Invoice> invoices = this.invoiceRepository.findAll();
	  	model.addAttribute("invoices", invoices);
		return "invoice_board";
	}

	//請求書作成ページに移動
	@GetMapping("/write")
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
	public String viewInvoice(@PathVariable Long id, Model model) {
	    List<Invoice> invoices = invoiceService.getInvoicebyId(id);
	    Invoice invoice = invoices.isEmpty() ? null : invoices.get(0);
	    model.addAttribute("invoice", invoice);
	    return "invoice_view";
	}
	
	
	//請求書削除
	@GetMapping("/delete/{id}")
	public String DeleteInvoice(Principal principal, @PathVariable("id") Long id) {
		
		Invoice invoice = this.invoiceService.getInvoice(id);

		this.invoiceService.delete(invoice);
		return "redirect:/invoice/board";
	}


}
