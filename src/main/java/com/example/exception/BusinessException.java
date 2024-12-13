package com.example.exception;

import lombok.Getter;

/**
 * 業務例外
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * メッセージキー
     */
    private final String messageKey;

    /**
     * @param messageKey
     */
    public BusinessException( String messageKey) {
        this.messageKey = messageKey;
    }
    
}
