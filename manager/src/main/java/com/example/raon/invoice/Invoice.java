package com.example.raon.invoice;

import java.time.LocalDate;

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

//請求書をDBに保存するfieldを定義
public class Invoice {
	
	/*
	 *invoiceId : 請求書のID
	 *invoiceTitle : 請求書のタイトル
	 *issuedDate : 発行日
	 *workhour :　基本勤務時間
	 *extraWorkhour :　時間
	 *deductionWorkhour :　時間
	 *unitPrice :　社員の時給単価
	 *companyName :　取引先
	 *address :　取引先の住所
	 *telephoneNumber : 取引先の住所
	 *employeeName :勤怠者
	 *tax : 税金
	 *charges : 総請求額
	 */

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

	private Integer tax;
	private Integer charges;

}
