package com.example.raon.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


//
@RequiredArgsConstructor
@Service
public class RaonUserSecurityService implements UserDetailsService  {

    private final RaonUserRepository raonUserReository;

    //
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RaonUser> _siteUser = this.raonUserReository.findByUsername(username);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("社員が見つかりません。");
        }
        RaonUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(RaonUserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(RaonUserRole.EMPLOYEE.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }


    

}