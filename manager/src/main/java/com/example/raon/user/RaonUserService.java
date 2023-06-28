package com.example.raon.user;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class RaonUserService {
	
	private final RaonUserReository raonUserReository ;
	private final PasswordEncoder passwordEncoder ; 
	
	public RaonUser create(String username, String userEmail, String password) {
		RaonUser user = new RaonUser();
		user.setUsername(username);
		user.setUserEmail(userEmail);
        user.setPassword(passwordEncoder.encode(password));
		this.raonUserReository.save(user);
		return user; 
	}
	
	

}
