package com.example.raon.employee;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@Service

//社員のビジネスロジク
public class EmployeeService {

	// 生成子
	private final EmployeeRepository employeeRepository;

	// 社員リスト-
	public Page<Employee> getList(Integer page, String kw) {

		//pageableオブジェクトを作成、1ページあたりのアイテム数を20に設定
		Pageable pageable = PageRequest.of(page, 20);
		
		//
		Specification<Employee> spec = searchByEmployee(kw);
		
		//Employeeリストを取得
		return this.employeeRepository.findAll(spec, pageable);

	}

	// IDで社員を検索
	public Employee getEmployeeID(Long id) {
		
		//指定したIDを持つ取引先情報を検索, Optionalオブジェクトを返します。
		Optional<Employee> employee = this.employeeRepository.findById(id);

		//
		return employee.get();

	}

	// 社員を登録
	public void enrollEmp(Long id, String name, String Ename, String Jname, String Pemail, String tel, String address,
			String acc, EmployeeBank bank, LocalDate join, LocalDate birth, Integer pay) {

		
		//Employeeオブジェクトに情報を設定
		Employee e = new Employee();
		
		e.setEmployeeId(id);
		e.setEmployeeName(name);
		e.setNameEng(Ename);
		e.setNameJp(Jname);
		e.setPersonalEmail(Pemail);
		e.setEmployeePhone(tel);
		e.setAddress(address);
		e.setBankAccount(acc);
		e.setBank(bank);

		e.setJoinDate(join);
		e.setBirthDate(birth);
		e.setPayDate(pay);

	    //社員情報をデータベースに保存
		this.employeeRepository.save(e);
	}

	// 社員を削除
	public void delete(Employee employee) {

		//社員情報をデータベースから削除
		this.employeeRepository.delete(employee);

	}

	// 社員情報を更新
	public void updateEmp(Long id, String name, String Ename, String Jname, String Pemail, String tel, String adress,
			String acc, EmployeeBank bank, LocalDate join, LocalDate birth, Integer pay) {
		
		//Employeeオブジェクトに情報を設定
		Employee e = new Employee();
		e.setEmployeeId(id);
		e.setEmployeeName(name);
		e.setNameEng(Ename);
		e.setNameJp(Jname);
		e.setPersonalEmail(Pemail);
		e.setEmployeePhone(tel);
		e.setAddress(adress);
		e.setBankAccount(acc);
		e.setBank(bank);
		e.setJoinDate(join);
		e.setBirthDate(birth);
		e.setPayDate(pay);

	    //社員情報をデータベースに保存
		this.employeeRepository.save(e);
	}

	
	// 検索
	public Specification<Employee> searchByEmployee(String kw) {
	    
		//無名クラスを使用して、Specification インターフェースの匿名実装を作成
		return new Specification<>() {
	    	
			//オブジェクトシリアル化
	        private static final long serialVersionUID = 1L;

	        //検索条件を生成するためのメソッド
	        @Override
	        public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	        	//クエリの結果から重複を排除
	        	query.distinct(true);

	        	// "employeeId","employeeName","NameEng","NameJp"がキーワードに部分一致する条件
	            Predicate idPredicate = cb.like(cb.lower(root.get("employeeId").as(String.class)), "%" + kw.toLowerCase() + "%");
	            Predicate namePredicate = cb.like(cb.lower(root.get("employeeName")), "%" + kw.toLowerCase() + "%");
	            Predicate nameEngPredicate = cb.like(cb.lower(root.get("NameEng")), "%" + kw.toLowerCase() + "%");
	            Predicate nameJpPredicate = cb.like(cb.lower(root.get("NameJp")), "%" + kw.toLowerCase() + "%");
	            
	            // "employeeId" または "employeeName" または "NameEng" または"NameJp" のいずれかが一致する場合を返す
	            return cb.or(idPredicate, namePredicate, nameEngPredicate, nameJpPredicate);
	        }
	    };
	}


}