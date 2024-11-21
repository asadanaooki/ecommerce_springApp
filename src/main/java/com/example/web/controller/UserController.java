package com.example.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.domain.model.result.UserRegistrationResult;
import com.example.service.user.PrefectureService;
import com.example.service.user.UserService;
import com.example.web.form.LoginForm;
import com.example.web.form.RegistrationForm;

import lombok.AllArgsConstructor;

/**
 * ユーザー関連のコントローラー
 */
@Controller
@AllArgsConstructor
public class UserController {

	/**
	 * ユーザー関連のサービス
	 */
	private final UserService userService;

	/**
	 * 都道府県データ関連のサービス
	 */
	private final PrefectureService prefectureService;

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

	/**
	 * ユーザー登録フォームを表示します。
	 * 
	 * @param form  ユーザー登録フォームオブジェクト
	 * @param model ビューに渡すデータ
	 * @return ユーザー登録画面のテンプレート名
	 */
	@GetMapping("user/register")
	public String showRegistrationForm(@ModelAttribute("form") RegistrationForm form, Model model) {
		addPrefectureList(model);
		return "user/registration";
	}

	/**
	 * ユーザー登録処理を行います。
	 * <p>
	 * 入力内容のバリデーションを行い、エラーがあれば登録画面を再表示します。 正常であれば一時的に登録情報を保存し、成功画面を表示します。
	 * </p>
	 * 
	 * @param form   ユーザー登録フォームオブジェクト
	 * @param result 入力検証の結果
	 * @param model  ビューに渡すデータ
	 * @return 次に表示するテンプレート名
	 */
	@PostMapping("user/register")
	public String registerTmpUser(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult result,
			Model model, HttpServletResponse response) {
		if (result.hasErrors()) {
			addPrefectureList(model);
			return "user/registration";
		}

		UserRegistrationResult regResult = userService.registerTempUser(form);
		// 登録エラー
		if (!regResult.isSuccess()) {
			regResult.getErrors().forEach(model::addAttribute);
			addPrefectureList(model);

			return "user/registration";
		}

		// クッキーの設定
		ResponseCookie cookie = ResponseCookie.from("userId", regResult.getUserId())
				.httpOnly(true).secure(true).sameSite("Strict").build();
		
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		
		return "redirect:/user/verificationCodeInput";
	}

	/**
	 * 都道府県のリストをモデルに追加します。
	 * 
	 * @param model ビューに渡すデータ
	 */
	private void addPrefectureList(Model model) {
		model.addAttribute("prefectureList", prefectureService.getAllPrefectures());
	}

}
