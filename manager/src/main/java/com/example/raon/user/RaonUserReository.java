package com.example.raon.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RaonUserReository extends JpaRepository<RaonUser, Long>{

    Optional<RaonUser> findByUsername(String username);
}
