package com.example.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性別を表す列挙型
 */
@AllArgsConstructor
@Getter
public enum Gender implements HasCode {
	/**
	 * 男
	 */
	MALE("M"),
	
	/**
	 * 女
	 */
	FEMALE("F");
	
	/**
	 * 性別コード
	 */
	private final String code;
	
}
