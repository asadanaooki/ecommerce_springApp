package com.example.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginForm {

	@NotBlank(message = "{error.email.required}")
	@Size(max = 100, message = "{error.email.length}")
	@Email(message = "{error.email.format}")
	private String email;

	@NotBlank(message = "{error.password.required}")
	@Size(min = 6, max = 20, message = "{error.password.length}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{error.password.format}")
	private String password;
}
