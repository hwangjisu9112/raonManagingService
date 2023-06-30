package com.example.raon.user;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserReository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public RaonUser create(String username, String password, Employee employee) {
        RaonUser user = new RaonUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Employee employeeEntity = employeeRepository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + employee.getEmployeeId()));

        user.setEmployee(employeeEntity);

        raonUserRepository.save(user);
        return user;
    }
}
