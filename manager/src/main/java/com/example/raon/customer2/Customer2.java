package com.example.raon.customer2;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer2 {

	@Id
	private Long CustomerId2;
	private String companyName2;
	private String Adress2; 
	private String PhoneNo2;
}
