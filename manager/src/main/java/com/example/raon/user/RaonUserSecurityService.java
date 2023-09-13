package com.example.raon.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

//
@RequiredArgsConstructor
@Service
public class RaonUserSecurityService implements UserDetailsService {

	private final RaonUserRepository raonUserRepository;

	//
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<RaonUser> _siteUser = this.raonUserRepository.findByUsername(username);
	    if (_siteUser.isEmpty()) {
	        throw new UsernameNotFoundException("使用者がいないです");
	    }
	    RaonUser siteUser = _siteUser.get();
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(siteUser.getRole().getValue()));

	    RaonUserCustomDetails userDetails = new RaonUserCustomDetails(siteUser, authorities);

	    return userDetails;
	}

}
