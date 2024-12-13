package com.example.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

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
    @Length(min = 6,max = 6,message = "{size.exact}")
    @Pattern(regexp = "^[0-9]+$",message = "{numeric.pattern}")
    private String code;
}
