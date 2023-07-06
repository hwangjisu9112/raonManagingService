package com.example.raon.invoice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.raon.customer.Customer;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

	private final InvoiceService invoiceService;

	@GetMapping("/board")
	public String BoardInvoice() {

		return "invoice_board";
	}

    @GetMapping("/write")
    public String writeInvoice(Model model) {
        List<Customer> customers = invoiceService.getAllCustomers();
        model.addAttribute("customers", customers);

        return "invoice_write";
    }
	
	
	@GetMapping("/view")
	public String ViewIvoice() {

		return "invoice_view";
	}

}
