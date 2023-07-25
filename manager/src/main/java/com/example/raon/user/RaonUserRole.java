package com.example.raon.user;

import lombok.Getter;

@Getter
public enum RaonUserRole {
	
    ADMIN("RAON_ADMIN"),
    EMPLOYEE("RAON_EMP");

	RaonUserRole(String value) {
        this.value = value;
    }

    private String value;

}
