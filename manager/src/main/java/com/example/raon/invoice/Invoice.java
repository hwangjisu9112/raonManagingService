package com.example.raon.invoice;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	private LocalDateTime issuedDate;
	
	private Integer totalWorkhour;
	
	private Integer extraWorkhour;
	
	private Integer deductionWorkhour;
	
	private Integer unitPrice;




}
