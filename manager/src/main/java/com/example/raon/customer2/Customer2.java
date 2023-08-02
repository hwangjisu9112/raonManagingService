package com.example.raon.customer2;

import java.time.LocalDate;

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
	private LocalDate Today; 
	private String Human;
	private String Business;
	private String Workingtime;
	private String Overtime;
	private String Losetime;
	public LocalDate Beginning;
	public LocalDate End1;
}
