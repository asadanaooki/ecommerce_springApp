package com.example.web.controller.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * エラーレスポンス
 */
@NoArgsConstructor
@Data
public class ErrorResponse {

    /**
     * エラーメッセージのリスト
     */
    private List<String> messages;

    /**
     * フィールドごとのエラーメッセージを保持するマップ
     */
    private Map<String, List<String>> targetMessages;

    /**
     * コンストラクタ
     * 
     * @param message エラーメッセージ
     */
    public ErrorResponse(String message) {
        addMessage(message);
    }

    /**
     * コンストラクタ
     * 
     * @param fieldName フィールド名
     * @param message   エラーメッセージ
     */
    public ErrorResponse(String fieldName, String message) {
        addTargetMessage(fieldName, message);
    }

    /**
     * エラーメッセージを追加する
     * 
     * @param message エラーメッセージ
     */
    public void addMessage(String message) {
        if (messages == null) {
            messages = new ArrayList<String>();
        }
        messages.add(message);
    }

    /**
     * フィールドごとのエラーメッセージを追加する
     * 
     * @param fieldName フィールド名
     * @param message   エラーメッセージ
     */
    public void addTargetMessage(String fieldName, String message) {
        if (targetMessages == null) {
            targetMessages = new HashMap<String, List<String>>();
        }
        List<String> messages = targetMessages.get(fieldName);

        if (messages == null) {
            messages = new ArrayList<String>();
            targetMessages.put(fieldName, messages);
        }
        messages.add(message);
    }

}
