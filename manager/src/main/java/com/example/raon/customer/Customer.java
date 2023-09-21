package com.example.raon.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

//取引先DBに保存するfieldを定義
public class Customer {
	
	/*
	 *CustomerId : 顧客会社に番号をつけて管理
	 *companyName : 顧客会社名
	 *Address : 顧客会の住所
	 *PhoneNo : 顧客会の電話番号
	 */

	@Id
	private Long CustomerId;
	private String companyName;
	private String Address; 
	private String PhoneNo;
	
	
}
