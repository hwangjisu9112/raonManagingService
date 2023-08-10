package com.example.raon.user;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserReository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    
    
    //社員リスト
    	public Page<RaonUser> getList(Integer page) {

    		Pageable pageable = PageRequest.of(page, 20);
    		return this.raonUserRepository.findAll(pageable);

    	}
      
  	//IDで社員メールを検索
  	public RaonUser getRaonUserID(Long id) {
  		Optional<RaonUser> raonUser = this.raonUserRepository.findById(id);

  		return raonUser.get();

  	}

    public RaonUser create(RaonUser raonUser) {
        raonUser.setPassword(passwordEncoder.encode(raonUser.getPassword()));

        Employee e = employeeRepository.findById(raonUser.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + raonUser.getEmployee().getEmployeeId()));

        raonUser.setEmployee(e);

        raonUserRepository.save(raonUser);
        return raonUser;
    }
}
