package com.example.service.user;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.domain.model.result.UserRegistrationResult;
import com.example.web.form.RegistrationForm;

import lombok.AllArgsConstructor;

/**
 * ユーザー関連のサービスクラス
 */
@Service
@AllArgsConstructor
public class UserService {

	/**
	 * メール認証関連の処理を担当するサービス
	 */
	private final MailVerificationService mailVerificationService;

	/**
	 * メッセージソース
	 */
	private final MessageSource messageSource;

	/**
	 * 仮ユーザーの登録を行う
	 * 
	 * @param form 登録情報
	 * @return 登録結果
	 */
	public UserRegistrationResult registerTempUser(RegistrationForm form) {
		UserRegistrationResult result = new UserRegistrationResult();

		// ロック状態確認
		if (mailVerificationService.isRegistrationLocked(form.getEmail())) {
			result.setSuccess(false);
			result.addError("global", messageSource.getMessage("registration.locked", null, null));
			return result;
		}

		// ユニーク制約チェック
		if (!mailVerificationService.checkUniqueConstraint(form, result)) {
			result.setSuccess(false);
			return result;
		}
		
		// Redisへ登録内容を保存する
		String userId = mailVerificationService.saveTempRegistrationInfo(form);

		// TODO: 認証メールを送信

		// 登録成功データを設定
		result.setUserId(userId);
		result.setSuccess(true);
		return result;
	}
}
