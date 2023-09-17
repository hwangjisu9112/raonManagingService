package com.example.raon.employee;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//社員のリポジトリ
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	// 社員を全て検索
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
    
	//名前で社員を検索
	Employee findByEmployeeName(String employeeName);
	
    // IDで社員を検索
    Employee findByEmployeeId(Long employeeId);
	
	// 社員を全て検索...請求書ページ用
    @Query("SELECT c FROM Employee c")
    List<Employee> getAllEmployees();


    
}
