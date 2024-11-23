package com.example.service.user;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.result.UserRegistrationResult;
import com.example.web.form.RegistrationForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

/**
 * ユーザー関連のサービスクラス
 */
@Service
@AllArgsConstructor
public class UserRegistrationService {

	/**
	 * メッセージソース
	 */
	private final MessageSource messageSource;

	/**
	 * 認証コードの最大値
	 */
	private final int MAX_CODE = 999999;

	/**
	 * ユーザー仮登録情報関連の接頭辞
	 */
	private final String MAIL_VERIFICATION_PREFIX = "mail_verification:";

	/**
	 * ロック情報キーの接頭辞
	 */
	private final String LOCK_PREFIX = "user_registration_lock:";

	/**
	 * Redis操作を行うためのテンプレート
	 */
	private final StringRedisTemplate template;

	/**
	 * オブジェクトマッパー
	 */
	private final ObjectMapper objectMapper;

	/**
	 * 仮登録の有効期限
	 */
	@Value("${registration.expiration-time-minutes}")
	public static int expirationTimeMinutes;

	/**
	 * ユーザー情報に関するDB操作を担当するマッパー
	 */
	private final UserMapper userMapper;

	/**
	 * 仮ユーザーの登録を行う
	 * 
	 * @param form 登録情報
	 * @return 登録結果
	 */
	public UserRegistrationResult registerTempUser(RegistrationForm form) {
		UserRegistrationResult result = new UserRegistrationResult();

		// ロック状態確認
		if (isRegistrationLocked(form.getEmail())) {
			result.setSuccess(false);
			result.addError("global", messageSource.getMessage("registration.locked", null, null));
			return result;
		}

		// ユニーク制約チェック
		if (!checkUniqueConstraint(form, result)) {
			result.setSuccess(false);
			return result;
		}

		// Redisへ登録内容を保存する
		Pair<String, String> pa = saveTempRegistrationInfo(form);

		// TODO: 認証メールを送信

		// 登録成功データを設定
		result.setUserId(pa.getFirst());
		result.setSuccess(true);
		return result;
	}

	/**
	 * 仮登録情報を保存する
	 * 
	 * @param form 登録フォーム
	 * @return 第一引数:ユーザーID 第二引数:認証コード
	 */
	 Pair<String, String> saveTempRegistrationInfo(RegistrationForm form) {
		String userId = UUID.randomUUID().toString();
		String code = String.format("%06d", new Random().nextInt(MAX_CODE + 1));
		Map<String, Object> map = objectMapper.convertValue(form, new TypeReference<Map<String, Object>>() {
		});
		map.put("code", code);

		// キー名を設定
		String userIdKey = MAIL_VERIFICATION_PREFIX + userId;

		// ユーザー情報を保存
		template.opsForHash().putAll(userIdKey, map);
		template.expire(userIdKey, expirationTimeMinutes, TimeUnit.MINUTES);

		return Pair.of(userId, code);
	}

	/**
	 * ユーザー登録がロックされてるか判定する
	 * 
	 * @param email メールアドレス
	 * @return true:ロック中 false:未ロック
	 */
	private boolean isRegistrationLocked(String email) {
		return template.hasKey(LOCK_PREFIX + email);
	}

	/**
	 * ユニーク制約をチェックする
	 * 
	 * @param form   登録情報
	 * @param result 登録結果
	 * @return true:ユニーク true:ユニークでない
	 */
	 boolean checkUniqueConstraint(RegistrationForm form, UserRegistrationResult result) {
		// eメールの制約
		if (isEmailRegistered(form.getEmail())) {
			result.addError("email", messageSource.getMessage("registration.email.duplicate", null, null));
			return false;
		}
		// 電話番号の制約
		if (isPhoneNumberRegistered(form.getPhoneNumber())) {
			result.addError("phoneNumber", messageSource.getMessage("registration.phoneNumber.duplicate", null, null));
			return false;
		}
		return true;
	}

	/**
	 * 電話番号が登録済みか判定する
	 * 
	 * @param phoneNumber 電話番号
	 * @return true:登録済み false:未登録
	 */
	private boolean isPhoneNumberRegistered(String phoneNumber) {
		return userMapper.findPhoneNumber(phoneNumber).isPresent();
	}

	/**
	 * メールアドレスが登録済みか判定する
	 * 
	 * @param email メールアドレス
	 * @return true:登録済み false:未登録
	 */
	private boolean isEmailRegistered(String email) {
		return userMapper.findEmail(email).isPresent();
	}
}
