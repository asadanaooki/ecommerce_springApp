package com.example.service.user;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.enums.Gender;
import com.example.web.form.RegistrationForm;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	MailVerificationService mailVerificationService;

	@Mock
	UserMapper userMapper;

	@InjectMocks
	UserService userService;

	RegistrationForm form;

	@Mock
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

	}

//	@Nested
//	class registerTempUser {
//		@Test
//		void registerTempUser_Success() {
//			when(mailVerificationService.isRegistrationLocked(form.getEmail())).thenReturn(false);
//			when(mailVerificationService.checkUniqueConstraint(eq(form), any(UserRegistrationResult.class)))
//					.thenReturn(true);
//
//			UserRegistrationResult result = userService.registerTempUser(form);
//
//			assertThat(result.isSuccess()).isTrue();
//		}
//
//		@Test
//		void registerTempUser_NotUnique() {
//			when(mailVerificationService.checkUniqueConstraint(eq(form), any(UserRegistrationResult.class)))
//			.thenReturn(false);
//
//			UserRegistrationResult result = userService.registerTempUser(form);
//
//			assertThat(result.isSuccess()).isFalse();
//		}
//
//	}

}
