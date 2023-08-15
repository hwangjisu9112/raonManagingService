package com.example.raon.user;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    
	//AuthCode
	private String generateRandomAuthCode() {
		
		return UUID.randomUUID().toString();
	}

    
    @Transactional
    public ResponseEntity<String> sendMailAndGenerateAuthCode(String username, String email) {
        Optional<RaonUser> optionalRaonUser = raonUserRepository.findByUsername(username);
        if (optionalRaonUser.isPresent()) {
            String authCode = generateRandomAuthCode();
            RaonUser raonUser = optionalRaonUser.get();
            raonUser.setAuthCode(authCode);
            raonUserRepository.save(raonUser); 
            sendResetPasswordEmail(email, authCode);
            return ResponseEntity.ok("認証メールが正常に送信されました。 メールを確認してください。");
        } else {
            return ResponseEntity.badRequest().body("存在しない会員です");
        }
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
    

    public boolean isAuthCodeValid(String authCode) {
        Optional<RaonUser> optionalRaonUser = raonUserRepository.findByAuthCode(authCode);
        return optionalRaonUser.isPresent();
    }

    public boolean resetPassword(String authCode, String newPassword) {
        Optional<RaonUser> optionalRaonUser = raonUserRepository.findByAuthCode(authCode);
        if (optionalRaonUser.isPresent()) {
            RaonUser raonUser = optionalRaonUser.get();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newPassword);

            raonUserRepository.updatePasswordByUsername(raonUser.getUsername(), encodedPassword);
            return true;
        }
        return false;
    }

    

}
