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

	@Id
	private Long CustomerId;
	private String companyName;
	//dk wkfahtcucuTek
	private String Adress; 
	private String PhoneNo;
}
