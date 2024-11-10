package com.example.web.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
	@NotBlank(message = "{registration.email.required}")
	@Size(max = 100, message = "{registration.email.size}")
	@Email(message = "{registration.email.format}")
	private String email;

	@NotBlank(message = "{registration.password.required}")
	@Size(min = 6, max = 20, message = "{registration.password.size}")
	private String password;

	@NotBlank(message = "{registration.lastNameKanji.required}")
	@Size(max = 20, message = "{registration.lastNameKanji.size}")
	private String lastNameKanji;

	@NotBlank(message = "{registration.firstNameKanji.required}")
	@Size(max = 20, message = "{registration.firstNameKanji.size}")
	private String firstNameKanji;

	@NotBlank(message = "{registration.lastNameKana.required}")
	@Size(max = 50, message = "{registration.lastNameKana.size}")
	private String lastNameKana;

	@NotBlank(message = "{registration.firstNameKana.required}")
	@Size(max = 50, message = "{registration.firstNameKana.size}")
	private String firstNameKana;

	@NotBlank(message = "{registration.gender.required}")
	private String gender;

	@NotNull(message = "{registration.birthDate.required}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "{registration.birthDate.format}")
	private LocalDate birthDate;

	@NotBlank(message = "{registration.postCode.required}")
	@Pattern(regexp = "\\d{7}", message = "{registration.postCode.format}") // 郵便番号は半角数字7桁
	private String postCode;

	@NotBlank(message = "{registration.prefecture.required}")
	private String prefectureId;

	@NotBlank(message = "{registration.address1.required}")
	@Size(max = 255, message = "{registration.address1.size}")
	private String address1;

	@Size(max = 255, message = "{registration.address2.size}")
	private String address2;

	@NotBlank(message = "{registration.phoneNumber.required}")
	@Pattern(regexp = "\\d{10,15}", message = "{registration.phoneNumber.format}") // 電話番号は10〜15桁の半角数字
	private String phoneNumber;

}
