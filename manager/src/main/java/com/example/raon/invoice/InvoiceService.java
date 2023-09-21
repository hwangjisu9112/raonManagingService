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

	/*
	*customerRepositoryを介してデータベース内のすべての顧客情報を取得
	*employeeRepositoryを介してデータベース内のすべてのemployee情報を取得
	*invoiceRepositoryを介してデータベース内のすべてのinvoiceId情報を取得
	*/
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public List<Invoice> getInvoicebyId(Long id) {
		return invoiceRepository.findByinvoiceId(id);
	}

	// Invoice ｐａｇｅリスト
	public Page<Invoice> getList(Integer page, String kw) {

		// pageableオブジェクトを生成, 1ページにつき10個のアイテム
		Pageable pageable = PageRequest.of(page, 10);

		//Specificationを作成
		Specification<Invoice> spec = searchByEmployee(kw);

		return this.invoiceRepository.findAll(spec, pageable);

	}

	// IDでinvoiceを検索
	public Invoice getInvoice(Long id) {
		
		//invoiceRepository を使用して、指定されたIDを持つ請求書をデータベースから検索。戻り値は Optional<Invoice> 型
		Optional<Invoice> invoice = this.invoiceRepository.findById(id);

		return invoice.get();

	}

	// 請求書登録
	public void write(String cpn, String add, String tel, String emp, String title, LocalDate date, Integer w,
			Integer ew, Integer dw, Integer price, Integer tax) {

		//新しい請求書オブジェクト（Invoice）を作成
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

		//請求書オブジェクトをデータベースに保存
		this.invoiceRepository.save(i);

	}

	// 請求書削除
	public void delete(Invoice invoice) {

	    // 請求書情報をデータベースから削除
		this.invoiceRepository.delete(invoice);

	}

	// 請求書検索
	public Specification<Invoice> searchByEmployee(String kw) {
	
		//無名クラスを使用して、Specification インターフェースの匿名実装を作成
		return new Specification<>() {
			
			//オブジェクトシリアル化
			private static final long serialVersionUID = 1L;

			//検索条件を生成するためのメソッド
			@Override
			public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				//クエリの結果から重複を排除
				query.distinct(true);

				// "companyName" がキーワードに部分一致する条件
				Predicate comPredicate = cb.like(cb.lower(root.get("companyName").as(String.class)),
						"%" + kw.toLowerCase() + "%");

	            // "comPredicate" のいずれかが一致する場合を返す
				return cb.or(comPredicate);
			}
		};
	}

}