package com.example.raon.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


//社員のリポジトリ
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	
	Page<Employee> findAll(Pageable pageable);
	Employee findByEmployeeName(String employeeName);
	

}
