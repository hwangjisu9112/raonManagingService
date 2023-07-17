package com.example.raon.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;
import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class InvoiceService {

	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final InvoiceRepository invoiceRepository;
	
	private final TemplateEngine templateEngine;

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public List<Invoice> getList() {
		return this.invoiceRepository.findAll();
	}

	public List<Invoice> getInvoicebyId(Long id) {
		return invoiceRepository.findByinvoiceId(id);
	}

	// IDでinvoiceを検索
	public Invoice getInvoice(Long id) {
		Optional<Invoice> invoice = this.invoiceRepository.findById(id);

		return invoice.get();

	}

	// touroku
	public void write(String cpn, String add, String tel, String emp, String title, LocalDate date, Integer w,
			Integer ew, Integer dw, Integer price, Integer tax) {

		Invoice i = new Invoice();
		i.setCompanyName(cpn);
		i.setAddress(add);
		i.setTelephoneNumber(tel);
		i.setEmployeeName(emp);
		i.setInvoiceTitle(title);
		i.setIssuedDate(date);
		i.setWorkhour(w);
		i.setExtraWorkhour(ew);
		i.setDeductionWorkhour(dw);
		i.setUnitPrice(price);
		i.setTax(tax);

		Integer crg = (w + ew - dw) * price * (100 - tax) / 100;

		i.setCharges(crg);

		this.invoiceRepository.save(i);

	}

	// 削除
	public void delete(Invoice invoice) {

		this.invoiceRepository.delete(invoice);

	}

	private void generatePdfFromInvoiceData(Invoice invoice) {
		try {
			Context context = new Context();
			context.setVariable("invoice", invoice);

			// Process the Thymeleaf template to HTML
			String htmlContent = templateEngine.process("your-template", context);

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);

			renderer.getSharedContext().setBaseURL("file:" + new ClassPathResource("static").getURL().getPath());

			// Generate the PDF
			try (OutputStream outputStream = new FileOutputStream("your-file-path/output.pdf")) {
				renderer.layout();
				renderer.createPDF(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Handle the exception as needed
		}

	}
}