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

    private final RaonUserService raonUserService;

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
}