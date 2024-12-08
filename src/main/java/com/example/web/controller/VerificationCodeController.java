package com.example.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.user.VerificationCodeService;
import com.example.web.form.VerificationCodeForm;

import lombok.AllArgsConstructor;

/**
 * 認証コード検証を担当するコントローラ
 */
@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class VerificationCodeController {

    /**
     * 認証コード関連の処理を行うサービスクラス
     */
    private final VerificationCodeService verificationCodeService;

    /**
     * 認証コード入力画面を表示する
     * @param form 入力フォーム
     * @param model モデル
     * @return 認証コード入力画面
     */
    @GetMapping("/verify-code")
    public String showVerificationCodeForm(@ModelAttribute("form") VerificationCodeForm form, Model model) {
        return "user/verificationCodeInput";
    }

    /**
     * 認証コードの検証を行う
     * @param userId ユーザーID
     * @param form 認証コードを保持するフォーム
     * @param result バリデーション結果
     * @param model モデル
     * @return 処理が成功すれば登録完了画面へリダイレクト
     */
    @PostMapping("/verify-code")
    public String verifyInputCode(@CookieValue(value = "userId", required = true) String userId,
            @Valid @ModelAttribute("form") VerificationCodeForm form, BindingResult result, Model model,HttpServletResponse response) {
        if (result.hasErrors()) {
            return showVerificationCodeForm(form, model);
        }

       verificationCodeService.processUserRegistration(userId, form);

        // クッキーからユーザーIDを削除する
       ResponseCookie cookie = ResponseCookie.from("userId","").maxAge(0).build();
       response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        return "redirect:/user/registrationComplete";
    }
    

    /**
     * ユーザー登録完了画面を表示する
     * @return ユーザー登録完了画面
     */
    @GetMapping("/registrationComplete")
    public String showRegistrationComplete() {
        return "user/registrationComplete";
    }

}
