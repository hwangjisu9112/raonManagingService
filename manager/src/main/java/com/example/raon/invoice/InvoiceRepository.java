package com.example.raon.invoice;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//請求書のレポジトリ
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	//請求書を全て検索
	Page<Invoice> findAll(Pageable pageable);

	
	//請求書をIDで検索
	List<Invoice> findByinvoiceId(Long invoiceId);

	
}
