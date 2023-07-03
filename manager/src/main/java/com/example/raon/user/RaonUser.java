package com.example.raon.user;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import com.example.raon.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Data
@Validated

public class RaonUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raonId;

    @Email
    @Column(unique = true)
    private String username;

    private String nameEmployee;

    @OneToOne
    @JoinColumn(name = "userId")
    private Employee employee;

    @Length(min=4)
    private String password;

    @Length(min=4)
    private String passwordRe;
}
