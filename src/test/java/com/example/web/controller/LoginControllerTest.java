package com.example.web.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.model.entity.Prefecture;
import com.example.domain.model.enums.Gender;
import com.example.domain.model.result.UserRegistrationResult;
import com.example.service.user.PrefectureService;
import com.example.service.user.UserRegistrationService;
import com.example.web.form.LoginForm;
import com.example.web.form.RegistrationForm;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

	// URLs
	static final String LOGIN_URL = "/login";
	static final String LOGIN_VIEW_NAME = "user/login";
	static final String PROCESS_LOGIN_URL = "/process-login";

	// Form Attributes
	static final String FORM_ATTRIBUTE = "form";

	// Field Names
	static final String FIELD_EMAIL = "email";
	static final String FIELD_PASSWORD = "password";
	static final String FIELD_LAST_NAME_KANJI = "lastNameKanji";
	static final String FIELD_FIRST_NAME_KANJI = "firstNameKanji";
	static final String FIELD_LAST_NAME_KANA = "lastNameKana";
	static final String FIELD_FIRST_NAME_KANA = "firstNameKana";
	static final String FIELD_GENDER = "gender";
	static final String FIELD_BIRTH_DATE = "birthDate";
	static final String FIELD_POST_CODE = "postCode";
	static final String FIELD_PREFECTURE_ID = "prefectureId";
	static final String FIELD_ADDRESS1 = "address1";
	static final String FIELD_ADDRESS2 = "address2";
	static final String FIELD_PHONE_NUMBER = "phoneNumber";

	// Common Test Values
	static final String EMPTY_STRING = "";
	static final String INVALID_EMAIL = "invalid-email";
	static final String VALID_EMAIL = "test@example.com";
	static final String VALID_PASSWORD = "password123"; // Can also be used for registration tests
	static final String SHORT_PASSWORD = "123"; // Less than 6 characters
	static final String LONG_PASSWORD = "1".repeat(25); // More than 20 characters
	static final String VALID_LAST_NAME_KANJI = "山田";
	static final String VALID_FIRST_NAME_KANJI = "太郎";
	static final String VALID_LAST_NAME_KANA = "ヤマダ";
	static final String VALID_FIRST_NAME_KANA = "タロウ";
	static final String VALID_GENDER = "M";
	static final String VALID_BIRTH_DATE = "1990-01-01";
	static final String FUTURE_BIRTH_DATE = "2090-01-01";
	static final String VALID_POST_CODE = "1234567";
	static final String VALID_PREFECTURE_ID = "13"; // 東京都
	static final String VALID_ADDRESS1 = "新宿区新宿1-1-1";
	static final String VALID_ADDRESS2 = "マンション101号室";
	static final String VALID_PHONE_NUMBER = "09012345678";
	static final String INVALID_PHONE_NUMBER = "090-123-45678"; // Contains hyphens
	static final String LONG_EMAIL = "a".repeat(101) + "@example.com"; // Over 100 characters
	static final String INVALID_PASSWORD = "invalid@password"; // Invalid format

	@Autowired
	MockMvc mockMvc;

	@Autowired
	MessageSource messageSource;

	List<Prefecture> prefectures;

	@MockBean
	PrefectureService prefectureService;

	@MockBean
	UserRegistrationService userRegistrationService;

	@BeforeEach
	void setUp() {
		prefectures = List.of(new Prefecture("1", "test1", 1, LocalDateTime.now(), LocalDateTime.now()),
				new Prefecture("2", "test2", 2, LocalDateTime.now(), LocalDateTime.now()),
				new Prefecture("3", "test3", 3, LocalDateTime.now(), LocalDateTime.now()));

		when(prefectureService.getAllPrefectures()).thenReturn(prefectures);
	}

	@Test
	void showLoginForm() throws Exception {
		mockMvc.perform(get(LOGIN_URL)).andExpect(status().isOk()).andExpect(view().name(LOGIN_VIEW_NAME))
				.andExpect(model().attributeExists(FORM_ATTRIBUTE));
	}

	@Test
	void loginSuccess() throws Exception {
		LoginForm form = new LoginForm(VALID_EMAIL, VALID_PASSWORD);

		ResultActions result = mockMvc.perform(post(LOGIN_URL).with(csrf()).flashAttr(FORM_ATTRIBUTE, form));

		result.andExpect(status().isOk()).andExpect(forwardedUrl(PROCESS_LOGIN_URL))
				.andExpect(model().attributeExists(FORM_ATTRIBUTE));
	}

	@ParameterizedTest
	@MethodSource("provideInvalidLoginData")
	void validateLoginForm(String email, String password, String expectedMessageKey, String expectedField)
			throws Exception {
		LoginForm form = new LoginForm(email, password);

		ResultActions action = mockMvc.perform(post(LOGIN_URL).with(csrf()).flashAttr(FORM_ATTRIBUTE, form));

		String expectedMessage = messageSource.getMessage(expectedMessageKey, null, null);

		ModelAndView mv = action.andReturn().getModelAndView();
		BindingResult result = (BindingResult) mv.getModel().get(BindingResult.MODEL_KEY_PREFIX + FORM_ATTRIBUTE);

		action.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, expectedField));

		assertTrue(result.getFieldErrors(expectedField).stream()
				.anyMatch(error -> Arrays.asList(error.getDefaultMessage()).contains(expectedMessage)));
	}

	static Stream<Arguments> provideInvalidLoginData() {
		return Stream.of(Arguments.of(EMPTY_STRING, VALID_PASSWORD, "login.failed", FIELD_EMAIL),
				Arguments.of(INVALID_EMAIL, VALID_PASSWORD, "login.failed", FIELD_EMAIL),
				Arguments.of(LONG_EMAIL, VALID_PASSWORD, "login.failed", FIELD_EMAIL),
				Arguments.of(VALID_EMAIL, EMPTY_STRING, "login.failed", FIELD_PASSWORD),
				Arguments.of(VALID_EMAIL, SHORT_PASSWORD, "login.failed", FIELD_PASSWORD),
				Arguments.of(VALID_EMAIL, LONG_PASSWORD, "login.failed", FIELD_PASSWORD),
				Arguments.of(VALID_EMAIL, INVALID_PASSWORD, "login.failed", FIELD_PASSWORD));
	}

	@Test
	void showRegistrationForm() throws Exception {
		mockMvc.perform(get("/user/register")).andExpect(status().isOk()).andExpect(view().name("user/registration"))
				.andExpect(model().attributeExists("form")).andExpect(model().attribute("prefectureList", prefectures));

	}

	@Nested
	class registrationTest {
		MultiValueMap<String, String> params;

		@BeforeEach
		void setup() {
			params = new LinkedMultiValueMap<>();
			params.add("email", "test@example.com");
			params.add("password", "password123");
			params.add("lastNameKanji", "山田");
			params.add("firstNameKanji", "太郎");
			params.add("lastNameKana", "ヤマダ");
			params.add("firstNameKana", "タロウ");
			params.add("gender", Gender.MALE.toString());
			params.add("birthDate", "1990-01-01"); // LocalDateは文字列に変換
			params.add("postCode", "1234567");
			params.add("prefectureId", "13"); // 東京都
			params.add("address1", "新宿区新宿1-1-1");
			params.add("address2", "マンション101号室");
			params.add("phoneNumber", "09012345678");

		}

		@Test
		void registerTmpUser_Success() throws Exception {
			UserRegistrationResult result = new UserRegistrationResult();
			result.setSuccess(true);

			when(userRegistrationService.registerTempUser(any(RegistrationForm.class))).thenReturn(result);

			mockMvc.perform(post("/user/register").with(csrf()).params(params)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/user/verificationCodeInput"));
		}

		@Test
		void registerTmpUser_ServiceFails() throws Exception {
			UserRegistrationResult regResult = new UserRegistrationResult();
			regResult.setSuccess(false);
			regResult.addError(FIELD_EMAIL, messageSource.getMessage("registration.email.duplicate", null, null));
			when(userRegistrationService.registerTempUser(any(RegistrationForm.class))).thenReturn(regResult);

			MvcResult mvcResult = mockMvc
					.perform(post("/user/register").with(csrf()).params(params)
							.contentType(MediaType.APPLICATION_FORM_URLENCODED))
					.andExpect(status().isOk()).andExpect(view().name("user/registration"))
					.andExpect(model().attribute(FIELD_EMAIL,
							messageSource.getMessage("registration.email.duplicate", null, null)))
					.andReturn();
		}

		@Test
		void registerTmpUser_CookieSet() throws Exception {
			UserRegistrationResult regResult = new UserRegistrationResult();
			regResult.setSuccess(true);
			String userId = UUID.randomUUID().toString();
			regResult.setUserId(userId);
			when(userRegistrationService.registerTempUser(any(RegistrationForm.class))).thenReturn(regResult);
			
			MvcResult result = mockMvc
					.perform(post("/user/register").with(csrf()).params(params)
							.contentType(MediaType.APPLICATION_FORM_URLENCODED))
					.andExpect(status().is3xxRedirection()).andReturn();

			String header = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);

			// クッキーの属性を検証
			assertThat(header).contains("userId=" + userId).contains("HttpOnly").contains("Secure")
			.contains("SameSite=Strict");
			
		}

		// TODO: 後で対応

//		@ParameterizedTest
//		@MethodSource("provideInvalidRegistrationData")
//		void validateRegistrationForm(String email, String password, String lastNameKanji, String firstNameKanji,
//				String lastNameKana, String firstNameKana, String gender, String birthDate, String postCode,
//				String prefectureId, String address1, String address2, String phoneNumber, String expectedMessageKey,
//				String expectedField) {
//
//		}
//
//		static Stream<Arguments> provideInvalidRegistrationData() {
//			return Stream.of(
//					// メールアドレス
//					Arguments.of(EMPTY_STRING, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.email.required", FIELD_EMAIL),
//					Arguments.of(INVALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.email.format", FIELD_EMAIL),
//					Arguments.of(LONG_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.email.size", FIELD_EMAIL),
//
//					// パスワード
//					Arguments.of(VALID_EMAIL, EMPTY_STRING, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.password.required", FIELD_PASSWORD),
//					Arguments.of(VALID_EMAIL, SHORT_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.password.size", FIELD_PASSWORD),
//
//					// 氏名（漢字）
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, EMPTY_STRING, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.lastNameKanji.required", FIELD_LAST_NAME_KANJI),
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, "あ".repeat(21), VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.lastNameKanji.size", FIELD_LAST_NAME_KANJI),
//
//					// 氏名（カナ）
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							EMPTY_STRING, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE, VALID_POST_CODE,
//							VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.lastNameKana.required", FIELD_LAST_NAME_KANA),
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							"ア".repeat(51), VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE, VALID_POST_CODE,
//							VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.lastNameKana.size", FIELD_LAST_NAME_KANA),
//
//					// 性別
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, EMPTY_STRING, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.gender.required", FIELD_GENDER),
//
//					// 生年月日
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, EMPTY_STRING, VALID_POST_CODE,
//							VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.birthDate.required", FIELD_BIRTH_DATE),
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, FUTURE_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.birthDate.format", FIELD_BIRTH_DATE),
//
//					// 郵便番号
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE, EMPTY_STRING,
//							VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.postCode.required", FIELD_POST_CODE),
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE, "12345",
//							VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.postCode.format", FIELD_POST_CODE),
//
//					// 都道府県
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, EMPTY_STRING, VALID_ADDRESS1, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.prefecture.required", FIELD_PREFECTURE_ID),
//
//					// 住所1
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, EMPTY_STRING, VALID_ADDRESS2, VALID_PHONE_NUMBER,
//							"registration.address1.required", FIELD_ADDRESS1),
//
//					// 住所2 (address2) のテストケース
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, "a".repeat(256), VALID_PHONE_NUMBER,
//							"registration.address2.size", FIELD_ADDRESS2),
//
//					// 電話番号
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, EMPTY_STRING,
//							"registration.phoneNumber.required", FIELD_PHONE_NUMBER),
//					Arguments.of(VALID_EMAIL, VALID_PASSWORD, VALID_LAST_NAME_KANJI, VALID_FIRST_NAME_KANJI,
//							VALID_LAST_NAME_KANA, VALID_FIRST_NAME_KANA, VALID_GENDER, VALID_BIRTH_DATE,
//							VALID_POST_CODE, VALID_PREFECTURE_ID, VALID_ADDRESS1, VALID_ADDRESS2, INVALID_PHONE_NUMBER,
//							"registration.phoneNumber.format", FIELD_PHONE_NUMBER));
//		}

	}

}
