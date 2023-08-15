package com.example.raon.user;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;



//ユーザーのビジネスロジク

@Transactional
@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserRepository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	private final ResourceLoader resourceLoader;

    //社員リスト
	public Page<RaonUser> getList(Integer page) {

		Pageable pageable = PageRequest.of(page, 20);
    	return this.raonUserRepository.findAll(pageable);

    	}
      
  	//IDで社員メールを検索
  	public RaonUser getRaonUserID(Long id) {
  		Optional<RaonUser> raonUser = this.raonUserRepository.findById(id);

  		return raonUser.get();

  	}

    public RaonUser create(RaonUser raonUser) {
        raonUser.setPassword(passwordEncoder.encode(raonUser.getPassword()));

        Employee e = employeeRepository.findById(raonUser.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + raonUser.getEmployee().getEmployeeId()));

        raonUser.setEmployee(e);

        raonUserRepository.save(raonUser);
        return raonUser;
    }
    
    public void sendResetPasswordEmail(String toEmail, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("RaonManagerパスワードリセットのご案内");

        String emailContent = loadEmailTemplate(authCode); // 이 부분을 수정
        message.setText(emailContent);

        javaMailSender.send(message);
    }
    
    private String loadEmailTemplate(String authCode) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/email-templates/send_authcode_mail.html");
            byte[] templateBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String template = new String(templateBytes, StandardCharsets.UTF_8);
            return template.replace("${authCode}", authCode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
    
//    private void saveAuthCodeToDatabase(String email, String authCode) {
//        Optional<RaonUser> optionalRaonUser = raonUserRepository.findByUsername(email);
//        if (optionalRaonUser.isPresent()) {
//            RaonUser raonUser = optionalRaonUser.get();
//            raonUser.setAuthCode(authCode);
//            raonUserRepository.save(raonUser);
//        }
//    }
    

}
