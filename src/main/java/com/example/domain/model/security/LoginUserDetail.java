package com.example.domain.model.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * アプリケーション固有のユーザー情報を管理する
 */
@Getter
public class LoginUserDetail extends User {

	/**
	 * メールアドレス
	 */
	private String email;

	/**
	 * コンストラクタ
	 * 
	 * @param userId      ユーザーID
	 * @param email       ユーザーのメールアドレス
	 * @param password    ユーザーのパスワード
	 * @param authorities ユーザーの権限情報
	 */
	public LoginUserDetail(String userId, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, password, authorities);
		this.email = email;
	}
}