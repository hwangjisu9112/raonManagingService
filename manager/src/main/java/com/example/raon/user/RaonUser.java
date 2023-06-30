package com.example.raon.user;

import org.hibernate.validator.constraints.Length;

import com.example.raon.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RaonUser {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raonId;

    
    //「springsecurityでloginするためには、変数名をusernameに定義する必要。。。」
    @Email
    @Column(unique = true)
    private String username;
 
    private String nameEmployee;
    
    @Length(min=4)
    private String password;
    
    @OneToOne(mappedBy = "user")
    private Employee employee;

}
