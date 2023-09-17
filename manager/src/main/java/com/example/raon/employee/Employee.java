package com.example.raon.employee;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

import com.example.raon.user.RaonUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

	/*
	 *employeeId : 社員ID
	 *employeeName :社員名前(英文)
	 *NameEng : 社員名前(日本語)
	 *NameJp : 社員名前
	 *PersonalEmail : 個人メール
	 *EmployeePhone : 個人の電話番号
	 *Address : 社員の住所
	 *bank : 給与を支払う銀行
	 *BankAccount : 銀行口座番号
	 *JoinDate : 入社日
	 *BirthDate :生年月日
	 *PayDate :給与日
	 *user : 1対1の関係でRaonUserと接続
	 */
	
	@Id
	@Positive
	private Long employeeId;

	private String employeeName;
	
	@Pattern(regexp = "[a-zA-Z]*", message = "英語で作成してください。")
	private String NameEng;

	@Pattern(regexp = "[\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}ー]*", message = "日本語で作成してください.")
	private String NameJp;

	@Email
	private String PersonalEmail;

	private String EmployeePhone;

	private String Address;

	// 給料を納金する口座は列挙Classで入力
	@Enumerated(EnumType.STRING)
	private EmployeeBank bank;

	private String BankAccount;

	private LocalDate JoinDate;
	@Past
	private LocalDate BirthDate;

	private Integer PayDate;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private RaonUser user;

}
