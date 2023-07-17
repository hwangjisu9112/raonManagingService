package com.example.raon.invoice;

import java.io.IOException;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.raon.customer.Customer;
import com.example.raon.employee.Employee;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

	private final InvoiceService invoiceService;
	private final InvoiceRepository invoiceRepository ;
    private final TemplateEngine templateEngine;



	@GetMapping("/board")
	public String BoardInvoice(Model model) {
	
		List<Invoice> invoices = this.invoiceRepository.findAll();
	  	model.addAttribute("invoices", invoices);
		return "invoice_board";
	}

	@GetMapping("/write")
	public String writeInvoice(Model model) {
	    List<Customer> customers = invoiceService.getAllCustomers();
	    List<Employee> employees = invoiceService.getAllEmployees();

	    model.addAttribute("customers", customers);
	    model.addAttribute("employees", employees);
	    model.addAttribute("invoice", new Invoice());

	    return "invoice_write";
	}

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

	@GetMapping("/view/{id}")
	public String viewInvoice(@PathVariable Long id, Model model) {
	    List<Invoice> invoices = invoiceService.getInvoicebyId(id);
	    Invoice invoice = invoices.isEmpty() ? null : invoices.get(0);
	    model.addAttribute("invoice", invoice);
	    return "invoice_view";
	}
	
	
	//削除
	@GetMapping("/delete/{id}")
	public String DeleteInvoice(Principal principal, @PathVariable("id") Long id) {
		
		Invoice invoice = this.invoiceService.getInvoice(id);

		this.invoiceService.delete(invoice);
		return "redirect:/invoice/board";
	}
	
	 @GetMapping("/pdf/{id}")
	    public void generatePdf(@PathVariable Long id, OutputStream outputStream) throws Exception {
	        Invoice invoice = invoiceService.getInvoice(id);

	        Context context = new Context();
	        context.setVariable("invoice", invoice);

	        String htmlContent = templateEngine.process("invoice_view", context);

	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocumentFromString(htmlContent);
	        renderer.layout();
	        renderer.createPDF(outputStream);
	    }
	

	

}
