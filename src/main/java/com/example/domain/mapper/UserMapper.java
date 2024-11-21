package com.example.domain.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.model.entity.User;

/**
 * ユーザー情報を操作するためのマッパー
 */
@Mapper
public interface UserMapper {

	/**
	 * 指定されたメールアドレスに基づいてユーザーの認証情報を取得
	 * 
	 * @param email メールアドレス
	 * @return ユーザーの認証情報
	 */
	Optional<User> findUserCredentialsByEmail(String email);

	/**
	 * 電話番号を取得する
	 * 
	 * @param phoneNumber 電話番号
	 * @return 電話番号
	 */
	Optional<String> findPhoneNumber(String phoneNumber);

	/**
	 * メールアドレスを取得する
	 * 
	 * @param email メールアドレス
	 * @return メールアドレス
	 */
	Optional<String> findEmail(String email);
}
