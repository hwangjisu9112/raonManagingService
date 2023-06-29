package com.example.raon.employee;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service

//社員のビジネスロジク
public class EmployeeService {

	//生成子
	private final EmployeeRepository employeeRepository;

	//社員リスト
	public Page<Employee> getList(Integer page) {

		Pageable pageable = PageRequest.of(page, 20);
		return this.employeeRepository.findAll(pageable);

	}

	//IDで社員を検索
	public Employee getEmployeeID(Long id) {
		Optional<Employee> employee = this.employeeRepository.findById(id);

		return employee.get();

	}

	//社員を登録
	public void enrollEmp(Long id, 
	                      String name, 
	                      String Ename, 
	                      String Jname, 
	                      String Pemail, 
	                      String tel, 
	                      String address, 
	                      String acc, 
	                      EmployeeBank bank,
	                      LocalDate join,
	                      LocalDate birth,
	                      Integer pay) {
	    
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
	    
	    this.employeeRepository.save(e);
	}

	//社員を削除
	public void delete(Employee employee) {

		this.employeeRepository.delete(employee);

	}

	//社員情報を更新
	public void updateEmp(Long id, 
						String name, 
						String Ename, 
						String Jname, 	
						String Pemail, 
						String tel, 
						String adress, 
						String acc, 
						EmployeeBank bank,
						LocalDate join,
						LocalDate birth,
						Integer pay) {
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
		
		this.employeeRepository.save(e);
	}

	//Paging処理
	public Page<Employee> getListPage(Integer page) {
		Pageable pageable = PageRequest.of(page, 20);
		return employeeRepository.findAll(pageable);
	}


}
