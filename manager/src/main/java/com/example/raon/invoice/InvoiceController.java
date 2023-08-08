package com.example.raon.invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.raon.customer.Customer;
import com.example.raon.employee.Employee;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

	private final InvoiceService invoiceService;
	private final InvoiceRepository invoiceRepository;
	private final TemplateEngine templateEngine; 

	
    Logger logger = Logger.getLogger(getClass().getName());


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
	public String writeInvoice(@RequestParam String cpn, @RequestParam String add, @RequestParam String tel,
			@RequestParam String emp, @RequestParam String title, @RequestParam LocalDate date, @RequestParam Integer w,
			@RequestParam Integer ew, @RequestParam Integer dw, @RequestParam Integer price,
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

	// 削除
	@GetMapping("/delete/{id}")
	public String DeleteInvoice(Principal principal, @PathVariable("id") Long id) {

		Invoice invoice = this.invoiceService.getInvoice(id);

		this.invoiceService.delete(invoice);
		return "redirect:/invoice/board";
		
	
	}
	
	
	@PostMapping("/generatePdf")
	public ResponseEntity<ByteArrayResource> generatePdfPost(@ModelAttribute("invoice") Invoice invoice) throws IOException {
	    Logger logger = Logger.getLogger(getClass().getName());

	    logger.log(Level.INFO, "Generating PDF for invoice: " + invoice);

	    // Create a ByteArrayOutputStream to hold PDF content
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    try {
	        // Create an ITextRenderer instance
	    	ITextRenderer renderer = new ITextRenderer();

	    	// Set up the HTML content
	    	String htmlContent = templateEngine.process("invoice_view", new Context(Locale.getDefault(), Collections.singletonMap("invoice", invoice)));

	    	// Configure the ITextRenderer
	    	renderer.setDocumentFromString(htmlContent);
	    	renderer.layout();

	    	// Render the PDF to the output stream
	    	renderer.createPDF(outputStream);
	    } catch (Exception e) {
	        logger.log(Level.SEVERE, "Error generating PDF", e);
	    } finally {
	        outputStream.close();
	    }

	    // Get the PDF bytes from the ByteArrayOutputStream
	    byte[] pdfBytes = outputStream.toByteArray();

	    // Return the PDF file to the client for download
	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF)
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
	            .body(new ByteArrayResource(pdfBytes));
	}
	
	
}
	

