package com.example.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザーの権限を表す列挙型
 */
@AllArgsConstructor
@Getter
public enum UserRole implements HasCode {
	/**
	 * 一般ユーザー
	 */
	USER("1", "USER"),
	
	/**
	 * 管理者
	 */
	MANAGER("2", "MANAGER");

	/**
	 * コード
	 */
	private final String code;
	

	/**
	 * 権限名
	 */
	private final String roleName;
}
