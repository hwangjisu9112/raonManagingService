package com.example.raon.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

//Spring Security ユーザーの認証および権限情報を保存

public class RaonUserCustomDetails extends User {
	
    
	private static final long serialVersionUID = -3609530364052414716L;
	
	private final RaonUser raonUser;

	
	//Spring SecurityのUserクラスを継承し、継承されたクラスの生成者を呼び出してユーザー情報を初期化
    public RaonUserCustomDetails(RaonUser raonUser, List<GrantedAuthority> authorities) {
        
    	//権限リストをSuperキーワードを介してUserクラスに配信
    	super(raonUser.getUsername(), raonUser.getPassword(), authorities);
        
    	//RaonUserオブジェクトをクラス内部のプロパティとして保存
    	this.raonUser = raonUser;
    }
    
    //RaonUserオブジェクトを返却
    public RaonUser getRaonUser() {
        return raonUser;
    }
}
