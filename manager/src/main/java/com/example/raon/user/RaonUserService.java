package com.example.raon.user;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserReository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    
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
    
    @Transactional
    public RaonUser createUserWithRole(RaonUser raonUser, RaonUserRole role) {
        raonUser.setPassword(passwordEncoder.encode(raonUser.getPassword()));

        Employee e = employeeRepository.findById(raonUser.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + raonUser.getEmployee().getEmployeeId()));

        raonUser.setEmployee(e);
        raonUser.setRole(role);

        return raonUserRepository.save(raonUser);
    }
    
	//社員メールを削除
	public void delete(RaonUser raonUser) {

		this.raonUserRepository.delete(raonUser);
		

	}
    
    
    
}
