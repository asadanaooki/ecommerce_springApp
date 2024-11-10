package com.example.web.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.domain.model.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー登録foam
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {

	/**
	 * メールアドレス
	 */
	@NotBlank(message = "{registration.email.required}")
	@Size(max = 100, message = "{registration.email.size}")
	@Email(message = "{registration.email.format}")
	private String email;

	/**
	 * パスワード
	 */
	@NotBlank(message = "{registration.password.required}")
	@Size(min = 6, max = 20, message = "{registration.password.size}")
	private String password;

	/**
	 * ユーザーの姓（漢字）
	 */
	@NotBlank(message = "{registration.lastNameKanji.required}")
	@Size(max = 20, message = "{registration.lastNameKanji.size}")
	@Pattern(regexp = "^[\\p{IsHan}]+$", message = "{registration.lastNameKanji.pattern}")
	private String lastNameKanji;

	/**
	 * ユーザーの名（漢字）
	 */
	@NotBlank(message = "{registration.firstNameKanji.required}")
	@Size(max = 20, message = "{registration.firstNameKanji.size}")
	@Pattern(regexp = "^[\\p{IsHan}]+$", message = "{registration.firstNameKanji.pattern}")
	private String firstNameKanji;

	/**
	 * ユーザーの姓（カナ）
	 */
	@NotBlank(message = "{registration.lastNameKana.required}")
	@Size(max = 50, message = "{registration.lastNameKana.size}")
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "{registration.lastNameKana.pattern}")
	private String lastNameKana;

	/**
	 * ユーザーの名（カナ）
	 */
	@NotBlank(message = "{registration.firstNameKana.required}")
	@Size(max = 50, message = "{registration.firstNameKana.size}")
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "{registration.firstNameKana.pattern}")
	private String firstNameKana;

	/**
	 * 性別
	 */
	@NotNull(message = "{registration.gender.required}")
	private Gender gender;

	/**
	 * 生年月日
	 */
	@NotNull(message = "{registration.birthDate.required}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "{registration.birthDate.format}")
	private LocalDate birthDate;

	/**
	 * 郵便番号
	 */
	@NotBlank(message = "{registration.postCode.required}")
	@Pattern(regexp = "\\d{7}", message = "{registration.postCode.format}") // 郵便番号は半角数字7桁
	private String postCode;

	/**
	 * 都道府県ID
	 */
	@NotBlank(message = "{registration.prefecture.required}")
	private String prefectureId;

	/**
	 * 住所1
	 */
	@NotBlank(message = "{registration.address1.required}")
	@Size(max = 255, message = "{registration.address1.size}")
	private String address1;

	/**
	 * 住所2
	 */
	@Size(max = 255, message = "{registration.address2.size}")
	private String address2;

	/**
	 * 電話番号
	 */
	@NotBlank(message = "{registration.phoneNumber.required}")
	@Pattern(regexp = "\\d{10,15}", message = "{registration.phoneNumber.format}") // 電話番号は10〜15桁の半角数字
	private String phoneNumber;

}
