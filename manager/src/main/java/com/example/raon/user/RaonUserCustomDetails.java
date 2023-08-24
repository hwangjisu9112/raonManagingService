package com.example.raon.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class RaonUserCustomDetails extends User {
	
    
	private static final long serialVersionUID = -3609530364052414716L;
	
	
	private final RaonUser raonUser;

    public RaonUserCustomDetails(RaonUser raonUser, List<GrantedAuthority> authorities) {
        super(raonUser.getUsername(), raonUser.getPassword(), authorities);
        this.raonUser = raonUser;
    }

    public RaonUser getRaonUser() {
        return raonUser;
    }
}
