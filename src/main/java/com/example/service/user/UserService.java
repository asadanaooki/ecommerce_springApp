package com.example.service.user;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.domain.mapper.UserMapper;
import com.example.service.result.RegistrationResult;
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
	 * ユーザー情報に関するDB操作を担当するマッパー
	 */
	private final UserMapper userMapper;

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
	public RegistrationResult registerTempUser(RegistrationForm form) {
		RegistrationResult result = new RegistrationResult();

		// ユニーク制約チェック
		if (!checkUniqueConstraint(form, result)) {
			result.setSuccess(false);
			return result;
		}

		Optional<String> codeOpt = mailVerificationService.generateVerificationCode();

		// 認証コードの生成が100回を超えた場合
		if (codeOpt.isEmpty()) {
			result.setSuccess(false);
			result.addError("email", messageSource.getMessage("registration.busy", null, null));
			return result;
		}

		// Redisへ登録内容を保存する
		mailVerificationService.saveTempRegistrationInfo(form, codeOpt.get());

		// TODO: 認証メールを送信

		// 登録成功フラグを設定
		result.setSuccess(true);
		return result;
	}

	/**
	 * ユニーク制約をチェックする
	 * 
	 * @param form   登録情報
	 * @param result 登録結果
	 * @return true:ユニーク true:ユニークでない
	 */
	private boolean checkUniqueConstraint(RegistrationForm form, RegistrationResult result) {
		// eメールの制約
		if (!isEmailUnique(form.getEmail())) {
			result.addError("email", messageSource.getMessage("registration.email.duplicate", null, null));
			return false;
		}
		// 電話番号の制約
		if (!isPhoneNumberUnique(form.getPhoneNumber())) {
			result.addError("phoneNumber", messageSource.getMessage("registration.phoneNumber.duplicate", null, null));
			return false;
		}
		return true;
	}

	/**
	 * 電話番号がユニークであるかを判定する
	 * 
	 * @param phoneNumber 電話番号
	 * @return true:ユニーク false:ユニークでない
	 */
	private boolean isPhoneNumberUnique(String phoneNumber) {
		return userMapper.isPhoneNumberUnique(phoneNumber);
	}

	/**
	 * メールアドレスがユニークか判定する
	 * 
	 * @param email メールアドレス
	 * @return true:ユニーク false:ユニークでない
	 */
	private boolean isEmailUnique(String email) {
		return userMapper.isEmailUnique(email);
	}

}
