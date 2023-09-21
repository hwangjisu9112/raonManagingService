package com.example.raon.user;

import lombok.Getter;

//ユーザーの権限を設定
@Getter
public enum RaonUserRole {

	ADMIN("ROLE_RAON_ADMIN"), 
	EMPLOYEE("ROLE_RAON_EMP");

	//(Raon User Role(String value))は役割の文字列値を受け取り、その役割のvalue変数に割り当て
	private String value;

	RaonUserRole(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
