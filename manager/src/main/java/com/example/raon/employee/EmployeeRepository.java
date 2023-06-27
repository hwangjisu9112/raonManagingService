package com.example.raon.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//社員のリポジトリ
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	// 社員を全て検索
	Page<Employee> findAll(Pageable pageable);

	//名前で社員を検索
	Employee findByEmployeeName(String employeeName);

}
