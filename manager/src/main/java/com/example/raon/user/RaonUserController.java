package com.example.raon.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;


@Controller
@RequestMapping("/raonuser")
@RequiredArgsConstructor
public class RaonUserController {

	private final RaonUserService raonuserService;

	@GetMapping("/signup")
	public String SignUp(Model model) {
		model.addAttribute("raonuserCreateForm", new RaonUserCreateForm());
		return "raon_signup";
	}

	@PostMapping("/signup")
	public String SignUp(@Valid RaonUserCreateForm raonUserCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "raon_signup";
		}

		if (!raonUserCreateForm.getPassword().equals(raonUserCreateForm.getPasswordRe())) {
			bindingResult.rejectValue("passwordRe", "passwordInCorrect", "パスワードが違います");
			return "raon_signup";
		}
	try {
		raonuserService.create(raonUserCreateForm.getUsername(), raonUserCreateForm.getUserEmail(),
				raonUserCreateForm.getPassword());
		
    }catch(DataIntegrityViolationException e) {
        e.printStackTrace();
        bindingResult.reject("signupFailed", "もう登録されているユーザーです。");
        return "raon_signup";
    }catch(Exception e) {
        e.printStackTrace();
        bindingResult.reject("signupFailed", e.getMessage());
        return "raon_signup";
    }

		return "redirect:/";
	}
	
	
	@GetMapping("/login")
	public String login() {
		
		  return "raon_login";
	}

}
