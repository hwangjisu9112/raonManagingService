package com.example.raon.user;

import org.hibernate.validator.constraints.Length;

import com.example.raon.employee.Employee;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RaonUserCreateForm {

    @Email
    @NotEmpty
    private String username;
    
    private String nameEmployee;

    @NotEmpty
    @Length(min = 4)
    private String password;

    @NotEmpty
    @Length(min = 4)
    private String passwordRe;

    private Employee employeeId;
    
   
}