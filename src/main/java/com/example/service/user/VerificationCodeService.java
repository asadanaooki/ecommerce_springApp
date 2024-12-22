package com.example.service.user;

import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.constant.UserConstant;
import com.example.domain.mapper.UserMapper;
import com.example.exception.BusinessException;
import com.example.service.user.converter.UserConverter;
import com.example.web.form.VerificationCodeForm;

import lombok.AllArgsConstructor;

/**
 * 認証コード照合サービス
 */
@Service
@AllArgsConstructor
public class VerificationCodeService {

    /**
     * Redisデータベースにアクセスするためのテンプレート
     */
    private final StringRedisTemplate template;

    /**
     * ユーザー情報を操作するためのマッパー
     */
    private final UserMapper userMapper;

    /**
     * 認証コードの最大試行回数
     */
    private final int MAX_ATTEMPT = 5;

    /**
     * エンコーダー
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * ユーザー登録プロセスを処理する
     * @param userId ユーザーID
     * @param form   ユーザーが入力した認証コードの情報
     */
    public void processUserRegistration(String userId, VerificationCodeForm form) {
        String key = UserConstant.MAIL_VERIFICATION_PREFIX + userId;

        // ユーザーの存在確認
        if (!template.hasKey(key)) {
            throw new BusinessException("verificationCode.expired");
        }
        
        Map<String, String> hash = template.<String, String> opsForHash().entries(key);
        
        // 照合成功時
        if (hash.get("code").equals(form.getCode())) {
            // ユーザー情報登録
            userMapper.insertSelective(UserConverter.toEntity(hash, passwordEncoder));
            // 仮登録情報を削除
            template.delete(key);
            return;
        }
        // 照合失敗時
        handleVerificationFailure(key, hash);
    }

    /**
     * 照合失敗時の処理を行う
     * @param key redisキー
     * @param hash ユーザー情報
     */
    private void handleVerificationFailure(String key, Map<String, String> hash) {
        // 試行回数をインクリメント
        int attempt = template.opsForHash().increment(key, "attempt", 1).intValue();

        // 最大試行回数に達した場合
        if (attempt >= MAX_ATTEMPT) {
            String lockKey = UserConstant.LOCK_PREFIX + hash.get("email");
            template.opsForValue().set(lockKey, "");
            // 仮登録情報を削除
            template.delete(key);

            throw new BusinessException("registration.locked");
        }
        // 最大試行回数に達していない場合
        throw new BusinessException("registration.invalid");
    }
}
