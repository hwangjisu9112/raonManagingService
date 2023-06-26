package com.example.raon.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer {

	@Id
	private Long CustomerId;
	private String companyName;
	private String Adress; 
	private String PhoneNo;
}
