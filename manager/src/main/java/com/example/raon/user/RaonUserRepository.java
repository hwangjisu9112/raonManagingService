package com.example.raon.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//ユーザーのリポジトリ

@Repository
public interface RaonUserRepository extends JpaRepository<RaonUser, Long>{

    Optional<RaonUser> findByUsername(String username);
    
    @Modifying
    @Query("UPDATE RaonUser u SET u.password = :newPassword WHERE u.username = :username")
    void updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
    
    Optional<RaonUser> findByAuthCode(String authCode);

}
