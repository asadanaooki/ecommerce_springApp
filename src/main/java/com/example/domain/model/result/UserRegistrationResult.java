package com.example.domain.model.result;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * ユーザー登録結果
 */
@Data
public class UserRegistrationResult {

	/**
	 * 登録成功フラグ
	 */
	private boolean success;

	/**
	 * エラー情報
	 */
	private final Map<String, String> errors = new HashMap<String, String>();
	
	/**
	 * ユーザーID
	 */
	private String userId;

	/**
	 * エラー情報を追加します。
	 * <p>
	 * フィールド名とエラーメッセージを対応付けてエラーリストに追加します。
	 * </p>
	 * 
	 * @param field   エラーが発生したフィールド名
	 * @param message フィールドに関連するエラーメッセージ
	 */
	public void addError(String field, String message) {
		errors.put(field, message);
	}
}
