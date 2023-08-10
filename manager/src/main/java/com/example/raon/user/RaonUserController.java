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

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;


@Controller
@RequestMapping("/raonuser")
@RequiredArgsConstructor
public class RaonUserController {

    private final RaonUserService raonUserService;
    
	// 社員ページに移動
	@GetMapping("/list")
	public String UserList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {
			page = 0;
		}

		Page<RaonUser> paging = this.raonUserService.getList(page);
		model.addAttribute("paging", paging);
		return "raon_list";
	}

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("raonUser", new RaonUser());
        return "raon_signup";
    }

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

    @GetMapping("/login")
    public String login() {
        return "raon_login";
    }
    
    @GetMapping("/reset")
    public String resetPassword() {
    	
    	
        return "raon_reset_password";
    }
    
    
}