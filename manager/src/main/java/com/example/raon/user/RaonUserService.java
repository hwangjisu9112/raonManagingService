package com.example.raon.user;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserRepository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;

    
    
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

        String emailContent = loadEmailTemplate(authCode);
        message.setText(emailContent);

        javaMailSender.send(message);
    }
    
    private String loadEmailTemplate(String authCode) {
        String template = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<title>RaonManagerパスワードリセット</title>"
                        + "</head>"
                        + "<body>"
                        + "<p>RaonManagerパスワードリセットのご案内</p>"
                        + "<p>以下のリンクをクリックして、パスワードのリセットを行ってください。</p>"
                        + "<a href=\"http://localhost:8080/raonuser/reset-password?authCode=${authCode}\">パスワードリセット</a>"
                        + "</body>"
                        + "</html>";
        
        return template.replace("${authCode}", authCode);
    }

}
