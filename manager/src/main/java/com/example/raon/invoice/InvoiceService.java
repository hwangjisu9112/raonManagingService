package com.example.raon.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.raon.customer.Customer;
import com.example.raon.customer.CustomerRepository;
import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

//請求書のビジネスロジク

public class InvoiceService {

	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final InvoiceRepository invoiceRepository;

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public List<Invoice> getInvoicebyId(Long id) {
		return invoiceRepository.findByinvoiceId(id);
	}

	// Invoice　ｐａｇｅリスト
	public Page<Invoice> getList(Integer page, String kw) {

		Pageable pageable = PageRequest.of(page, 10);
		Specification<Invoice> spec = searchByEmployee(kw);

		return this.invoiceRepository.findAll(spec, 
				pageable);

	}

	// IDでinvoiceを検索
	public Invoice getInvoice(Long id) {
		Optional<Invoice> invoice = this.invoiceRepository.findById(id);

		return invoice.get();

	}

	// 登録
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
		// 総請求金額
		Integer crg = (w + ew - dw) * price * (100 - tax) / 100;

		i.setCharges(crg);

		this.invoiceRepository.save(i);

	}

	// 削除
	public void delete(Invoice invoice) {

		this.invoiceRepository.delete(invoice);

	}
	

	// 検索
	public Specification<Invoice> searchByEmployee(String kw) {
	    return new Specification<>() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	            query.distinct(true);

	            Predicate comPredicate = cb.like(cb.lower(root.get("companyName").as(String.class)), "%" + kw.toLowerCase() + "%");
	     
	            return cb.or(comPredicate);
	        }
	    };
	}


}