package com.example.web.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.web.form.LoginForm;

@Controller
public class UserController {

	@GetMapping("/login")
	public String showLoginForm(@ModelAttribute("form") LoginForm form) {
		return "auth/login";
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("form") LoginForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "auth/login";
		}
		return "forward:/process-login";
	}
	
}
