package com.example.raon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String index() {
		return "redirect:/main";
	}
	
	 // main page
    @GetMapping("/main")
    public String Main() {
        return "main_page";
    }

}
