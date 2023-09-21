package com.example.raon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


//
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	
	//Spring Security フィルターチェーンを構成
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		//すべてのリクエストに対して認証なしでアクセスを許可
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
				.and()
				
				//Cross-Site Request Forgery (CSRF) 攻撃から保護するための設定
				.csrf()
				
				//特定のURLパターンに対するCSRF保護を無効にする
				.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-manage/**"),
						new AntPathRequestMatcher("/raonuser/**"))
				.and()
				.headers()
				
				//X-Frame-Optionsヘッダーを設定し、同じ出典からページをロード
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				.and()
				
				//フォームベースのログイン設定
				.formLogin().loginPage("/raonuser/login").defaultSuccessUrl("/")
				.and()
				
				//ログアウト関連設定
				.logout()

				//ログアウト成功後、基本的に移動するURL
				.logoutRequestMatcher(new AntPathRequestMatcher("/raonuser/logout")).logoutSuccessUrl("/")
				
				//HTTPセッションを無効化
				.invalidateHttpSession(true);
		return http.build();

	}

	//パスワードのハッシュと検証のために使用
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Spring Security の認証（ログイン）を管理
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	

	



}