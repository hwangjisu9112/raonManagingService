package com.example.raon.employee;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

import com.example.raon.user.RaonUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Validated

//社員DBに保存するfieldを定義
public class Employee {

	@Id
	@Positive
	private Long EmployeeId;

	private String employeeName;
	@Pattern(regexp = "[a-zA-Z]*", message = "英語で作成してください。")
	private String NameEng;

	@Pattern(regexp = "[\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}ー]*", message = "日本語で作成してください.")
	private String NameJp;

	@Email
	private String EmployeeEmail;
	@Email
	private String PersonalEmail;

	private String EmployeePhone;

	private String Address;

	// 給料を納金する口座は列挙Classで入力
	@Enumerated(EnumType.STRING)
	private EmployeeBank bank;

	private String BankAccount;
	@Positive
	private Integer WorkRate;

	private LocalDate JoinDate;

	private LocalDate BirthDate;

	private Integer PayDate;

	@OneToOne
	@JoinColumn(name = "user_id")
	private RaonUser user;

}
