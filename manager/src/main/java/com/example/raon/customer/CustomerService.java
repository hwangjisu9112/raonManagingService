package com.example.raon.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

//取引先のビジネスロジク
public class CustomerService {

	private final CustomerRepository customerRepository;

	// 取引先のリストを全て検索
	public Page<Customer> getList(Integer page, String kw) {

		Pageable pageable = PageRequest.of(page, 5);
		Specification<Customer> spec = searchByCust(kw);

		return this.customerRepository.findAll(spec, pageable);

	}

	// IDで取引先を検索
	public Customer getCustomer(Long id) {
		Optional<Customer> customer = this.customerRepository.findById(id);

		return customer.get();

	}

	// 社名で取引先を検索
	public Customer getCustomerByCompanyName(String name) {
		Customer customer = customerRepository.findByCompanyName(name);
		return customer;
	}

	// 取引先を登録
	public void enroll(Long id, String name, String address, String phone) {
		Customer c = new Customer();
		c.setCustomerId(id);
		c.setCompanyName(name);
		c.setAddress(address);
		c.setPhoneNo(phone);
		this.customerRepository.save(c);

	}

	// 取引先を削除
	public void delete(Customer customer) {

		this.customerRepository.delete(customer);
	}

	// 取引先を更新
	public void update(Customer customer, Long id, String name, String address, String phone) {
		customer.setCustomerId(id);
		customer.setCompanyName(name);
		customer.setAddress(address);
		customer.setPhoneNo(phone);
		this.customerRepository.save(customer);
	}

	// 検索
	public Specification<Customer> searchByCust(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);

				Predicate idPredicate = cb.like(cb.lower(root.get("CustomerId").as(String.class)),
						"%" + kw.toLowerCase() + "%");
	            Predicate companyPredicate = cb.like(cb.lower(root.get("companyName")), "%" + kw.toLowerCase() + "%");

			
				return cb.or(idPredicate, companyPredicate);
			}
		};
	}

}
