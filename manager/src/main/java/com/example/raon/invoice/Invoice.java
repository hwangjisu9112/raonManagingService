package com.example.raon.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long invoiceId;

	private String invoiceTitle;

	private LocalDate issuedDate;
	@Positive
	private Integer workhour;
	private Integer extraWorkhour;
	private Integer deductionWorkhour;
	@Positive
	private Integer unitPrice;

	private String companyName;
	private String address;
	private String telephoneNumber;
	private String employeeName;
	
	private Integer tax ;
	private Integer charges ; 

}
