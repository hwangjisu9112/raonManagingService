package com.example.raon.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.raon.employee.Employee;

//ユーザーのリポジトリ
@Repository
public interface RaonUserRepository extends JpaRepository<RaonUser, Long>{

	
	// 社員を全て検索
    Page<RaonUser> findAll(Specification<RaonUser> spec, Pageable pageable);

	
    Optional<RaonUser> findByUsername(String username);
    
//	// IDで検索
//	List<RaonUser> findByAttendCode(Long attendCode);
    
    @Modifying
    @Query("UPDATE RaonUser u SET u.password = :newPassword WHERE u.username = :username")
    void updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
    
    Optional<RaonUser> findByAuthCode(String authCode);

}
