package com.example.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.form.LoginForm;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	static final String LOGIN_URL = "/login";
	static final String LOGIN_VIEW_NAME = "auth/login";
	static final String FORM_ATTRIBUTE = "form";
	static final String PROCESS_LOGIN_URL = "/process-login";

	static final String TEST_EMAIL = "test@example.com";
	static final String TEST_PASSWORD = "testpassword";

	static final String FIELD_EMAIL = "email";
	static final String FIELD_PASSWORD = "password";

	static final String ERROR_EMAIL_REQUIRED = "error.email.required";
	static final String ERROR_EMAIL_FORMAT = "error.email.format";
	static final String ERROR_EMAIL_LENGTH = "error.email.length";
	static final String ERROR_PASSWORD_REQUIRED = "error.password.required";
	static final String ERROR_PASSWORD_LENGTH = "error.password.length";
	static final String ERROR_PASSWORD_FORMAT = "error.password.format";

	static final String EMPTY_STRING = "";
	static final String INVALID_EMAIL = "invalid-email";
	static final String VALID_PASSWORD = "validPass123";
	static final String LONG_EMAIL = "a".repeat(101) + "@example.com";
	static final String SHORT_PASSWORD = "123";
	static final String LONG_PASSWORD = "1".repeat(25);
	static final String INVALID_PASSWORD = "invalid@password";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	MessageSource messageSource;

	@Test
	void showLoginForm() throws Exception {
		mockMvc.perform(get(LOGIN_URL)).andExpect(status().isOk()).andExpect(view().name(LOGIN_VIEW_NAME))
				.andExpect(model().attributeExists(FORM_ATTRIBUTE));
	}

	@Test
	void loginSuccess() throws Exception {
		LoginForm form = new LoginForm(TEST_EMAIL, TEST_PASSWORD);

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
		return Stream.of(Arguments.of(EMPTY_STRING, VALID_PASSWORD, ERROR_EMAIL_REQUIRED, FIELD_EMAIL),
				Arguments.of(INVALID_EMAIL, VALID_PASSWORD, ERROR_EMAIL_FORMAT, FIELD_EMAIL),
				Arguments.of(LONG_EMAIL, VALID_PASSWORD, ERROR_EMAIL_LENGTH, FIELD_EMAIL),
				Arguments.of(TEST_EMAIL, EMPTY_STRING, ERROR_PASSWORD_REQUIRED, FIELD_PASSWORD),
				Arguments.of(TEST_EMAIL, SHORT_PASSWORD, ERROR_PASSWORD_LENGTH, FIELD_PASSWORD),
				Arguments.of(TEST_EMAIL, LONG_PASSWORD, ERROR_PASSWORD_LENGTH, FIELD_PASSWORD),
				Arguments.of(TEST_EMAIL, INVALID_PASSWORD, ERROR_PASSWORD_FORMAT, FIELD_PASSWORD));
	}
}
