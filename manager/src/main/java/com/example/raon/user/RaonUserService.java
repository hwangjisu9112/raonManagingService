package com.example.raon.user;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class RaonUserService {
	
	private final RaonUserReository raonUserReository ;
    private final EmployeeRepository employeeRepository;

	private final PasswordEncoder passwordEncoder ; 
	
	
    public RaonUser create(String username, String password, Long employeeId) {
        RaonUser user = new RaonUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + employeeId));

        user.setEmployee(employee);

        raonUserReository.save(user);
        return user;
    }

}
