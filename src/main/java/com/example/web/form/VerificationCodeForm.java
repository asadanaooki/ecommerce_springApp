package com.example.web.form;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 認証コード入力フォーム
 */
@Data
@AllArgsConstructor
public class VerificationCodeForm {

    /**
     * 認証コード
     */
    @NotBlank(message = "{input.required}")
    @Digits(integer = 6, fraction = 0, message = "{numeric.format}")
    private String code;
}
