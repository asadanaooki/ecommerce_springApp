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

	@NotBlank(message = "{login.failed}")
	@Size(max = 100, message = "{login.failed}")
	@Email(message ="{login.failed}")
	private String email;

	@NotBlank(message = "{login.failed}")
	@Size(min = 6, max = 20, message = "{login.failed}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{login.failed}")
	private String password;
}
