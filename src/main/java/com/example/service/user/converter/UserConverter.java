package com.example.service.user.converter;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.domain.model.entity.User;
import com.example.web.form.RegistrationForm;

/**
 * ユーザー関連のデータ変換を行うユーティリティクラス
 */
public class UserConverter {

	/**
	 * 登録formをエンティティに変換する
	 * 
	 * @param form            ユーザー登録フォーム
	 * @param passwordEncoder パスワードエンコーダー
	 * @return ユーザーエンティティ
	 */
	public static User toEntity(RegistrationForm form, PasswordEncoder passwordEncoder) {
		User user = new User();
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setFirstNameKanji(form.getFirstNameKanji());
		user.setLastNameKanji(form.getLastNameKanji());
		user.setFirstNameKana(form.getFirstNameKana());
		user.setLastNameKana(form.getLastNameKana());
		user.setGender(form.getGender());
		user.setBirthDate(form.getBirthDate());
		user.setPostCode(form.getPostCode());
		user.setPrefectureId(form.getPrefectureId());
		user.setAddress1(form.getAddress1());
		user.setAddress2(form.getAddress2());
		user.setPhoneNumber(form.getPhoneNumber());
		return user;
	}

}
