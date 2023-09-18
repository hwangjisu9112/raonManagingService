package com.example.raon.user;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import com.example.raon.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Data
@Validated

//社内イントラネットを利用するためのDBに保存するfieldを定義

public class RaonUser {
	
	/*
	 *raonId : ラオンユーザーの自動的に付与されるID
	 *username : メールアドレス形式のID
	 *nameEmployee : 名
	 *attendCode : 出退勤時に使われる社員番号と同額
	 *employee : 
	 *password : パスワード
	 *passwordRe : 会員登録時のパスワード確認
	 *role : 社員の管理権限
	 *authCode : パスワードを再発行するためのコード
	 */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raonId;

    //usernameじゃないとエラーが出る··· スプリングセキュリティ自体の性質

    @Email
    @Column(unique = true)
    private String username;

    private String nameEmployee;
    
    private Long attendCode;
    
    //既存に登録した社員IDと一致しないと登録できません
    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private Employee employee;

    @Length(min=4)
    private String password;

    @Length(min=4)
    private String passwordRe;
    
    
    //社員の権限等級を設定
    @Enumerated(EnumType.STRING)
    private RaonUserRole role;
    
    //パスワード修正の為に伝送する
    private String authCode;
    
}