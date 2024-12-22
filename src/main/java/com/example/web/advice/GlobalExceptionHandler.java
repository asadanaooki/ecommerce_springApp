package com.example.web.advice;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.exception.BusinessException;
import com.example.web.controller.error.ErrorResponse;

import lombok.AllArgsConstructor;

/**
 * 例外をハンドラするクラス
 */
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    /**
     * メッセージソース
     */
    private MessageSource messageSource;

    /**
     * 業務例外をハンドリングする
     * @param e 業務例外
     * @return エラーレスポンス
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        // TODO: SSRと共通化し、ErrorResponseも修正する
        
        String message = messageSource.getMessage(e.getMessageKey(), null, null);
        ErrorResponse response = new ErrorResponse(message);
        
        return ResponseEntity.badRequest().body(response);

    }

    /**
     * バリデーションエラーをハンドリングする
     * @param e BindException
     * @return エラーレスポンス
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        ErrorResponse response = new ErrorResponse();

        // エラー情報取得
        for (FieldError fieldError : e.getFieldErrors()) {
            String errorMessage = messageSource.getMessage(fieldError, null);
            response.addTargetMessage(fieldError.getField(), errorMessage);
        }
        return ResponseEntity.badRequest().body(response);
    }

}
