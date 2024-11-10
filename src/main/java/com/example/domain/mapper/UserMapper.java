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
	 * 指定された電話番号が一意であるか判定する
	 * 
	 * @param phoneNumber 電話番号
	 * @return true:一意である false:重複している
	 */
	boolean isPhoneNumberUnique(String phoneNumber);

	/**
	 * 指定されたメールアドレスがデータベース内で一意であるか判定する
	 * 
	 * @param email メールアドレス
	 * @return true:一意である false:重複している
	 */
	boolean isEmailUnique(String email);
}
