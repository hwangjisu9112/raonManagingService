package com.example.raon.bill;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

//請求書DBに保存するfieldを定義
public class Customer2 {

	@Id
	private Long CustomerId2;
	private String companyName2;
	private String Adress2; 
	private String PhoneNo2;
}
