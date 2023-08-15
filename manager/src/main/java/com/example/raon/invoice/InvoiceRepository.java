package com.example.raon.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

//請求書のレポジトリ
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	//請求書をIDで検索
	List<Invoice> findByinvoiceId(Long invoiceId);

	
}
