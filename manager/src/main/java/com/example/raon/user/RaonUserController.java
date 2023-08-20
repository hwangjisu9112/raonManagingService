package com.example.raon.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/raonuser")
@RequiredArgsConstructor

//社内ユーザーメールのコントローラー
public class RaonUserController {

	//生成子
	private final RaonUserService raonUserService;
	private final RaonUserRepository raonUserRepository;

	// ページに移動
	@GetMapping("/list")
	public String userList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {
			page = 0;
		}

		Page<RaonUser> paging = this.raonUserService.getList(page);
		model.addAttribute("paging", paging);
		return "raon_list";
	}

	// 社内メールを登録するページに移動
	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("raonUser", new RaonUser());
		return "raon_signup";
	}

	// 社内メールを登録
	@PostMapping("/signup")
	public String signUp(@Valid RaonUser raonUser, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "raon_signup";
		}

		if (!raonUser.getPassword().equals(raonUser.getPasswordRe())) {
			bindingResult.rejectValue("passwordRe", "passwordIncorrect", "パスワードが違います");
			return "raon_signup";
		}

		try {
			raonUserService.create(raonUser);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "もう登録されているユーザーです。");
			return "raon_signup";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "raon_signup";
		}

		return "redirect:/";
	}

	// ログイン、　ビジネスロジクはSPRING securityで実装
	@GetMapping("/login")
	public String login() {
		return "raon_login";
	}
	
	// パスワード修正するページに移動
	@GetMapping("/reset-sendmail")
	public String sendMailpage() {

		return "raon_reset_sendmail";
	}
	

	// パスワード修正する為にAuthCodeをメールで発送
	@PostMapping("/reset-sendmail")
	public ResponseEntity<String> sendMailpage(@RequestParam("username") String username,
	                                               @RequestParam("email") String email) {
	        
		 return raonUserService.sendMailAndGenerateAuthCode(username, email);
	}

	// AuthCodeを確認するページ    
	@GetMapping("/reset-authcode")
	public String checkAuthCodePage() {

		return "raon_reset_authcode";
	}
	
	// AuthCodeを確認する

	 @PostMapping("/reset-authcode")
	 public String checkAuthCodePage(@RequestParam("authCode") String authCode) {
	        Optional<RaonUser> oru = raonUserRepository.findByAuthCode(authCode);
	        if (oru.isPresent()) {
	            return "redirect:/raonuser/reset-password?authCode=" + authCode;
	        } else {
	            return "redirect:/raonuser/reset-authcode?error=invalid";
	        }
	    }

	    @GetMapping("/reset-password")
	    public String resetPasswordPage(@RequestParam("authCode") String authCode, Model model) {
	        model.addAttribute("authCode", authCode);
	        return "raon_reset_password";
	    }
	    
	    @PostMapping("/reset-password")
	    public ResponseEntity<String> resetPassword(@RequestParam("authCode") String authCode,
	                                                @RequestParam("newPassword") String newPassword) {
	        if (raonUserService.resetPassword(authCode, newPassword)) {
	            return ResponseEntity.ok("パスワードのリセットが完了しました。");
	        } else {
	            return ResponseEntity.badRequest().body("AutoCodeが違います。");
	        }
	    }
	    
//	    @Transactional
//	    @PostMapping("/reset-password")
//	    public ResponseEntity<String> resetPasswordChecked(@RequestParam("authCode") String authCode,
//	                                                       @RequestParam("newPassword") String newPassword) {
//	        Optional<RaonUser> oru = raonUserRepository.findByAuthCode(authCode);
//	        if (oru.isPresent()) {
//	            RaonUser raonUser = oru.get();
//
//	            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//	            String encodedPassword = passwordEncoder.encode(newPassword);
//
//	            raonUserRepository.updatePasswordByUsername(raonUser.getUsername(), encodedPassword);
//	            return ResponseEntity.ok("パスワードのリセットが完了しました。");
//	        } else {
//	            return ResponseEntity.badRequest().body("AutoCodeが違います。");
//	        }
//	    }

}