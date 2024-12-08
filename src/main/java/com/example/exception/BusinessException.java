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
     * 遷移先画面
     */
    private final String viewName;


    /**
     * @param messageKey
     * @param viewName
     */
    public BusinessException( String messageKey, String viewName) {
        this.messageKey = messageKey;
        this.viewName = viewName;
    }
    
    

}
