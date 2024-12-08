package com.example.web.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.domain.model.enums.Gender;
import com.example.domain.model.enums.UserRole;

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
	@NotBlank(message = "{input.required}")
	@Size(max = 255, message = "{size.max}")
	@Email(message = "{email.format}")
	private String email;

	/**
	 * パスワード
	 */
	@NotBlank(message = "{input.required}")
	@Size(min = 6, max = 20, message = "{size.range}")
	private String password;

	/**
	 * ユーザーの姓（漢字）
	 */
	@NotBlank(message = "{input.required}")
	@Size(max = 20, message = "{size.max}")
	@Pattern(regexp = "^[\\p{IsHan}]+$", message = "{kanji.pattern}")
	private String lastNameKanji;

	/**
	 * ユーザーの名（漢字）
	 */
	@NotBlank(message = "{input.required}")
	@Size(max = 20, message = "{size.max}")
	@Pattern(regexp = "^[\\p{IsHan}]+$", message = "{kanji.pattern}")
	private String firstNameKanji;

	/**
	 * ユーザーの姓（カナ）
	 */
	@NotBlank(message = "{input.required}")
	@Size(max = 50, message = "{size.max}")
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "{kana.pattern}")
	private String lastNameKana;

	/**
	 * ユーザーの名（カナ）
	 */
	@NotBlank(message = "{input.required}")
	@Size(max = 50, message = "{size.max}")
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "{kana.pattern}")
	private String firstNameKana;

	/**
	 * 性別
	 */
	@NotNull(message = "{input.required}")
	private Gender gender;

	/**
	 * 生年月日
	 */
	@NotNull(message = "{input.required}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "{birthDate.format}")
	private LocalDate birthDate;

	/**
	 * 郵便番号
	 */
	@NotBlank(message = "{input.required}")
	@Digits(integer = 7, message = "{numeric.format}", fraction = 0)
	private String postCode;

	/**
	 * 都道府県ID
	 */
	@NotBlank(message = "{input.required}")
	private String prefectureId;

	/**
	 * 住所1
	 */
	@NotBlank(message = "{input.required}")
	@Size(max = 255, message = "{size.max}")
	private String address1;

	/**
	 * 住所2
	 */
	@Size(max = 255, message = "{size.max}")
	private String address2;

	/**
	 * 電話番号
	 */
	@NotBlank(message = "{input.required}")
	@Pattern(regexp = "\\d{10,15}", message = "{phoneNumber.format}") // 電話番号は10〜15桁の半角数字
	private String phoneNumber;

	
	/**
	 * 権限
	 */
	private UserRole role = UserRole.USER;

}
