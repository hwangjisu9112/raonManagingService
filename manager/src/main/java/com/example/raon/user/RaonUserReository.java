package com.example.raon.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RaonUserReository extends JpaRepository<RaonUser, Long>{

	// 社員を全て検索
	Page<RaonUser> findAll(Pageable pageable);
	
    Optional<RaonUser> findByUsername(String username);
}
