package com.example.raon.invoice;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Invoice {
	
	@Id
	private Long invoiceId;
	
	private String invoiceTitle; 
	
	private LocalDateTime issuedDate;
	

}
