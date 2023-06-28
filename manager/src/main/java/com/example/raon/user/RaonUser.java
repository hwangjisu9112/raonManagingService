package com.example.raon.user;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(unique = true)
    private String username;
 
    @Length(min=4)
    private String password;
    
    @Email
    private String userEmail;


}
