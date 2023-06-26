package com.example.raon.employee;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

//社員DBに保存するfieldを定義
public class Employee {

	@Id
	private Long EmployeeId;

	private String employeeName;

	private String NameEng;
	
	private String NameJp;

	private String EmployeeEmail;

	private String PersonalEmail;

	private String EmployeePhone;

	private String Address;

	//給料を納金する口座は列挙Classで入力
	@Enumerated(EnumType.STRING)
	private EmployeeBank bank;
	
	private String BankAccount;

	private Integer WorkRate;

	private LocalDate JoinDate;

	private LocalDate BirthDate;

	private Integer PayDate;
	
	private Long CustomerId;

}
