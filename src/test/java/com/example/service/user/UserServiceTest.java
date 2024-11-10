package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.enums.Gender;
import com.example.service.result.RegistrationResult;
import com.example.web.form.RegistrationForm;

@SpringBootTest
class UserServiceTest {

	@MockBean
	MailVerificationService mailVerificationService;

	@MockBean
	UserMapper userMapper;

	@Autowired
	UserService userService;

	RegistrationForm form;

	@Autowired
	MessageSource messageSource;

	@BeforeEach
	void setUp() {
		form = new RegistrationForm();
		form.setEmail("test@example.com");
		form.setPassword("password123");
		form.setLastNameKanji("山田");
		form.setFirstNameKanji("太郎");
		form.setLastNameKana("ヤマダ");
		form.setFirstNameKana("タロウ");
		form.setGender(Gender.MALE);
		form.setBirthDate(LocalDate.of(1990, 1, 1));
		form.setPostCode("1234567");
		form.setPrefectureId("01");
		form.setAddress1("Shibuya-ku");
		form.setAddress2("Building 101");
		form.setPhoneNumber("09012345678");

		doNothing().when(mailVerificationService).saveTempRegistrationInfo(form, "mail_verification:100050");
	}

	@Test
	void registerTempUser_Success() {
		when(userMapper.isEmailUnique(form.getEmail())).thenReturn(true);
		when(userMapper.isPhoneNumberUnique(form.getPhoneNumber())).thenReturn(true);
		when(mailVerificationService.generateVerificationCode()).thenReturn(Optional.of("mail_verification:100050"));

		RegistrationResult result = userService.registerTempUser(form);

		assertThat(result.isSuccess()).isTrue();
	}

	@Test
	void registerTempUser_EmailExist() {
		when(userMapper.isEmailUnique(form.getEmail())).thenReturn(false);

		RegistrationResult result = userService.registerTempUser(form);

		assertThat(result.isSuccess()).isFalse();
		assertThat(result.getErrors())
				.containsExactly(entry("email", messageSource.getMessage("registration.email.duplicate", null, null)));
	}

	@Test
	void registerTempUser_PhoneNumberExist() {
		when(userMapper.isEmailUnique(form.getEmail())).thenReturn(true);
		when(userMapper.isPhoneNumberUnique(form.getEmail())).thenReturn(false);

		RegistrationResult result = userService.registerTempUser(form);

		assertThat(result.isSuccess()).isFalse();
		assertThat(result.getErrors()).containsExactly(
				entry("phoneNumber", messageSource.getMessage("registration.phoneNumber.duplicate", null, null)));
	}

	@Test
	void registerTempUser_exceedsVerificationCodeLimit() {
		when(userMapper.isEmailUnique("test@example.com")).thenReturn(true);
		when(userMapper.isPhoneNumberUnique("09012345678")).thenReturn(true);
		when(mailVerificationService.generateVerificationCode()).thenReturn(Optional.empty());

		RegistrationResult result = userService.registerTempUser(form);
		
		assertThat(result.isSuccess()).isFalse();
		assertThat(result.getErrors().get("email")).isEqualTo(messageSource.getMessage("registration.busy", null, null));
	}

}
