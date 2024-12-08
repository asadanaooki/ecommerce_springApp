package com.example.service.user.converter;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.domain.model.entity.User;
import com.example.domain.model.enums.Gender;
import com.example.domain.model.enums.UserRole;
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

    /**
     * redisに保存されたデータをUserエンティティに変換する
     * @param hash ユーザー情報を格納したMap
     * @param encoder エンコーダ
     * @return Userエンティティ
     */
    public static User toEntity(Map<String, String> hash, PasswordEncoder passwordEncoder) {
        User user = new User();

        user.setUserId(hash.get("userId"));
        user.setEmail(hash.get("email"));
        user.setPassword(passwordEncoder.encode(hash.get("password")));
        user.setFirstNameKanji(hash.get("firstNameKanji"));
        user.setLastNameKanji(hash.get("lastNameKanji"));
        user.setFirstNameKana(hash.get("firstNameKana"));
        user.setLastNameKana(hash.get("lastNameKana"));
        user.setPostCode(hash.get("postCode"));
        user.setPrefectureId(hash.get("prefectureId"));
        user.setAddress1(hash.get("address1"));
        user.setPhoneNumber(hash.get("phoneNumber"));

        // address2が設定されてる場合のみセットする
        if (!hash.get("address2").isEmpty()) {
            user.setAddress2(hash.get("address2"));
        }
        // 性別
        user.setGender(Gender.valueOf(hash.get("gender")));
        // 権限
        user.setRole(UserRole.valueOf(hash.get("role")));
        // 生年月日
        user.setBirthDate(LocalDate.parse(hash.get("birthDate")));

        ;
        return user;
    }

}
