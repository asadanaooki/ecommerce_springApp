package com.example.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.domain.model.enums.Gender;
import com.example.domain.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザーエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	/**
	 * ユーザーID
	 */
	private String userId;

	/**
	 * メールアドレス
	 */
	private String email;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * 漢字の氏名
	 */
	private String firstNameKanji;

	/**
	 * 漢字の名字
	 */
	private String lastNameKanji;

	/**
	 * カナの氏名
	 */
	private String firstNameKana;

	/**
	 * カナの名字
	 */
	private String lastNameKana;

	/**
	 * 性別
	 */
	private Gender gender;

	/**
	 * 生年月日
	 */
	private LocalDate birthDate;

	/**
	 * 郵便番号
	 */
	private String postCode;

	/**
	 * 都道府県ID
	 */
	private String prefectureId;

	/**
	 * 住所1
	 */
	private String address1;

	/**
	 * 住所2
	 */
	private String address2;

	/**
	 * 電話番号
	 */
	private String phoneNumber;

	/**
	 * 権限
	 */
	private UserRole role;

	/**
	 * 作成日時
	 */
	private LocalDateTime createdAt;

	/**
	 * 更新日時
	 */
	private LocalDateTime updatedAt;
}