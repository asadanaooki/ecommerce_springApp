package com.example.service.user;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.web.form.RegistrationForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * メール認証に関連するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class MailVerificationService {

	/**
	 * 認証コードの最小値
	 */
	private final int CODE_MIN = 100000;

	/**
	 * 認証コードの範囲
	 */
	private final int CODE_RANGE = 900000;

	/**
	 * 認証コード生成の最大試行回数
	 */
	private final int MAX_ATTEMPTS = 100;

	/**
	 * 認証コードのキーの接頭辞
	 */
	private final String MAIL_VERIFICATION_PREFIX = "mail_verification:";

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
	 * 認証コードを生成
	 * 
	 * @return 認証コード
	 */
	public Optional<String> generateVerificationCode() {
		Random random = new Random();
		int attempt = 1;

		// 試行回数が100回まではコードの再生成を行う
		while (attempt <= MAX_ATTEMPTS) {
			int code = random.nextInt(CODE_RANGE) + CODE_MIN;
			String key = MAIL_VERIFICATION_PREFIX + code;

			if (!template.hasKey(key)) {
				return Optional.of(key);
			}
			attempt++;
		}
		return Optional.empty();
	}

	/**
	 * 仮登録情報を保存する
	 * 
	 * @param form 登録フォーム
	 * @param key  認証コードのキー
	 */
	public void saveTempRegistrationInfo(RegistrationForm form, String key) {
		Map<String, String> map = objectMapper.convertValue(form, new TypeReference<Map<String, String>>() {
		});
		template.opsForHash().putAll(key, map);
		template.expire(key, expirationTimeMinutes, TimeUnit.MINUTES);
	}
}
