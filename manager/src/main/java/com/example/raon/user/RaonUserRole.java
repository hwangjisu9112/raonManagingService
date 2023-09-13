package com.example.raon.user;

import lombok.Getter;

//ユーザーの権限を設定
@Getter
public enum RaonUserRole {

	ADMIN("ROLE_RAON_ADMIN"), 
	EMPLOYEE("ROLE_RAON_EMP");

	private String value;

	RaonUserRole(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
