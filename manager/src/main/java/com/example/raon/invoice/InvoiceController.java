package com.example.raon.invoice;

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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.itextpdf.layout.Document;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

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
	public ResponseEntity<ByteArrayResource> generatePdfPost(@ModelAttribute("invoice") Invoice invoice, HttpServletResponse response) throws DocumentException, IOException {

	    logger.log(Level.INFO, "Generating PDF for invoice: " + invoice);

	    // Create a ByteArrayOutputStream to hold PDF content
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    // Create a PDF document
	    PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
	    Document document = new Document(pdfDocument);

	    String htmlContent = templateEngine.process("invoice_view", new Context(Locale.getDefault(), Collections.singletonMap("invoice", invoice)));

	    logger.log(Level.INFO, "HTML content: " + htmlContent);

	    // Convert HTML to PDF using iText 7's HtmlConverter
	    ConverterProperties converterProperties = new ConverterProperties();
	    HtmlConverter.convertToPdf(htmlContent, pdfDocument, converterProperties);

	    // Close the document
	    document.close();

	    // Get the PDF bytes from the ByteArrayOutputStream
	    byte[] pdfBytes = outputStream.toByteArray();

	    // Return the PDF file to the client
	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF)
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
	            .body(new ByteArrayResource(pdfBytes));
	}
	
	
}

