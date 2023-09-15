package com.example.raon.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

		// pageableオブジェクトを作成、1ページあたりのアイテム数を5に設定
		Pageable pageable = PageRequest.of(page, 5);

		// searchByCustメソッドを呼び出し、検索キーワード kw を基にしてCustomerエンティティを検索する仕様を生成します。
		Specification<Customer> spec = searchByCust(kw);

		// customerRepositoryのfindAllメソッドを呼び出してデータを取得。
		return this.customerRepository.findAll(spec, pageable);

	}

	// IDで取引先を検索
	public Customer getCustomer(Long id) {

		//指定したIDを持つ取引先情報を検索, Optionalオブジェクトを返します。
		Optional<Customer> customer = this.customerRepository.findById(id);

		return customer.get();

	}
	
//	// 社名で取引先を検索이제 필요없지 않나?
//	public Customer getCustomerByCompanyName(String name) {
//		Customer customer = customerRepository.findByCompanyName(name);
//		return customer;
//	}

	// 取引先を登録
	public void enroll(Long id, String name, String address, String phone) {
		Customer c = new Customer();
		
	    // Customerオブジェクトに情報を設定
		c.setCustomerId(id);
		c.setCompanyName(name);
		c.setAddress(address);
		c.setPhoneNo(phone);
		
	    // 取引先情報をデータベースに保存
		this.customerRepository.save(c);

	}

	// 取引先を削除
	public void delete(Customer customer) {

	    // 取引先情報をデータベースから削除
		this.customerRepository.delete(customer);
	}

	// 取引先を更新
	public void update(Customer customer, Long id, String name, String address, String phone) {
		

	    // Customerオブジェクトに情報を更新
		customer.setCustomerId(id);
		customer.setCompanyName(name);
		customer.setAddress(address);
		customer.setPhoneNo(phone);
		
		// 取引先情報をデータベースに更新
		this.customerRepository.save(customer);
	}

	// 検索
	public Specification<Customer> searchByCust(String kw) {
		
		//無名クラスを使用して、Specification インターフェースの匿名実装を作成
		return new Specification<>() {
			
			//オブジェクトシリアル化
			private static final long serialVersionUID = 1L;

			//検索条件を生成するためのメソッド
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				//クエリの結果から重複を排除
				query.distinct(true);

				// "CustomerId" がキーワードに部分一致する条件
				Predicate idPredicate = cb.like(cb.lower(root.get("CustomerId").as(String.class)),
						"%" + kw.toLowerCase() + "%");
				
				 // "companyName" がキーワードに部分一致する条件
				Predicate companyPredicate = cb.like(cb.lower(root.get("companyName")), "%" + kw.toLowerCase() + "%");

				
	            // "CustomerId" または "companyName" のいずれかが一致する場合を返す
				return cb.or(idPredicate, companyPredicate);
			}
		};
	}

}
