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

    public RaonUser create(String username, String ename, String password, Employee employeeId) {
        RaonUser u = new RaonUser();
        u.setUsername(username);
        u.setNameEmployee(ename);
        u.setPassword(passwordEncoder.encode(password));

        Employee e = employeeRepository.findById(employeeId.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + employeeId.getEmployeeId()));

        u.setEmployee(e);

        raonUserRepository.save(u);
        return u;
    }
}
