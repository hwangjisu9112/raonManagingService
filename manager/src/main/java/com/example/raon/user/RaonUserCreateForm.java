package com.example.raon.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaonUserCreateForm {
	@NotEmpty
	private String username;
	@NotEmpty
	@Length(min=4)
	private String password;
	@NotEmpty
	@Length(min=4)
	private String passwordRe;

	@Email
	@NotEmpty
	private String userEmail;

}
