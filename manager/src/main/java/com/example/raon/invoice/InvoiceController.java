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

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

import com.example.raon.customer.Customer;
import com.example.raon.employee.Employee;




import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

	private final InvoiceService invoiceService;
	private final InvoiceRepository invoiceRepository;

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
	
	    @GetMapping("/pageScreenshot")
	    @ResponseBody
	    public String pageScreenshot() {
	        // 크롬 드라이버 경로 설정
	        System.setProperty("webdriver.chrome.driver", "C:\\Users\\crist\\Documents\\workspaceForRaon\\manager\\ChromeDriver.exe");

	        // 크롬 브라우저 옵션 설정 (headless 모드)
	        ChromeOptions options = new ChromeOptions();
	        options.setHeadless(true);

	        // WebDriver 객체 생성
	        WebDriver driver = new ChromeDriver(options);

	        try {
	            // 웹 페이지 접속
	            driver.get("http://example.com");

	            // 스크린샷 캡쳐
	            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	            return "스크린샷이 성공적으로 캡쳐되었습니다.";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "스크린샷 캡쳐 중 오류가 발생했습니다.";
	        } finally {
	            // WebDriver 종료
	            driver.quit();
	        }
	    }


}

