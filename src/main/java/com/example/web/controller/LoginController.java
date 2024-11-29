package com.example.web.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.web.form.LoginForm;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー関連のコントローラー
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

	/**
	 * ログイン画面を表示
	 * 
	 * @param form ログイン情報
	 * @return ログイン画面のテンプレート
	 */
	@GetMapping("/login")
	public String showLoginForm(@ModelAttribute("form") LoginForm form) {
		return "user/login";
	}

	/**
	 * ログイン処理を行います。
	 * <p>
	 * 入力内容のバリデーションを行い、エラーがあればログイン画面を再表示します。 正常であればログイン処理を他のエンドポイントに転送します。
	 * </p>
	 * 
	 * @param form   ログインフォームオブジェクト
	 * @param result 入力検証の結果
	 * @param model  ビューに渡すデータ
	 * @return 次に表示するテンプレートまたは処理
	 */
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("form") LoginForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "user/login";
		}
		return "forward:/process-login";
	}

}
