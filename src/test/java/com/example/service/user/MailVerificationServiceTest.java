package com.example.service.user;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.enums.Gender;
import com.example.web.form.RegistrationForm;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class MailVerificationServiceTest {

	@Mock
	StringRedisTemplate template;

	@Mock
	HashOperations<String, Object, Object> hashOperations;

	@Mock
	UserMapper userMapper;

	@InjectMocks
	MailVerificationService mailVerificationService;

	@Mock
	MessageSource messageSource;

	@Mock
	ObjectMapper objectMapper;

	RegistrationForm form;

	@BeforeEach
	void setup() {
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

//	@Test
//	void saveTempRegistrationInfo() {
//		UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		Map<String, Object> map = new ObjectMapper().convertValue(form, new TypeReference<Map<String, Object>>() {});
//		
//		try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
//			mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);
//			when(objectMapper.convertValue(eq(form), any(TypeReference.class))).thenReturn(map);
//
//			@SuppressWarnings("unchecked")
//			ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
//			when(template.opsForHash()).thenReturn(hashOperations);
//
//			mailVerificationService.saveTempRegistrationInfo(form);
//
//			verify(template.opsForHash()).putAll(eq(fixedUUID.toString()), captor.capture());
//			verify(template).expire(eq(fixedUUID.toString()), MailVerificationService.expirationTimeMinutes,
//					TimeUnit.MINUTES);
//			Map<String, Object> capMap = captor.getValue();
//
//			assertThat(capMap.get("email")).isEqualTo("test@example.com");
//			assertThat(capMap.get("password")).isEqualTo("password123");
//			assertThat(capMap.get("lastNameKanji")).isEqualTo("山田");
//			assertThat(capMap.get("firstNameKanji")).isEqualTo("太郎");
//			assertThat(capMap.get("lastNameKana")).isEqualTo("ヤマダ");
//			assertThat(capMap.get("firstNameKana")).isEqualTo("タロウ");
//			assertThat(capMap.get("gender")).isEqualTo(Gender.MALE.toString());
//			assertThat(capMap.get("birthDate")).isEqualTo(LocalDate.of(1990, 1, 1).toString());
//			assertThat(capMap.get("postCode")).isEqualTo("1234567");
//			assertThat(capMap.get("prefectureId")).isEqualTo("01");
//			assertThat(capMap.get("address1")).isEqualTo("Shibuya-ku");
//			assertThat(capMap.get("address2")).isEqualTo("Building 101");
//			assertThat(capMap.get("phoneNumber")).isEqualTo("09012345678");
//
//		}
//
//	}

	//@Nested
//	class isRegistrationLocked {
//		@Test
//		void isRegistrationLocked_AlradyExist() {
//			when(template.hasKey("user_registration_lock:" + form.getEmail())).thenReturn(true);
//
//			boolean result = mailVerificationService.isRegistrationLocked(form.getEmail());
//			assertThat(result).isTrue();
//		}
//
//		@Test
//		void isRegistrationLocked_NotExist() {
//			when(template.hasKey("user_registration_lock:" + form.getEmail())).thenReturn(false);
//
//			boolean result = mailVerificationService.isRegistrationLocked(form.getEmail());
//			assertThat(result).isFalse();
//		}
//	}
//
//	@Nested
//	class checkUniqueConstraint {
//		@Test
//		void checkUniqueConstraint_AllUnique() {
//			UserRegistrationResult result = new UserRegistrationResult();
//			when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.empty());
//			when(userMapper.findPhoneNumber(form.getPhoneNumber())).thenReturn(Optional.empty());
//
//			boolean isUnique = mailVerificationService.checkUniqueConstraint(form, result);
//
//			assertThat(isUnique).isTrue();
//		}
//
//		@Test
//		void checkUniqueConstraint_EmailRegistered() {
//			UserRegistrationResult result = new UserRegistrationResult();
//			when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.of(form.getEmail()));
//			when(messageSource.getMessage("registration.email.duplicate", null, null)).thenReturn("error");
//
//			boolean isUnique = mailVerificationService.checkUniqueConstraint(form, result);
//
//			assertThat(isUnique).isFalse();
//			assertThat(result.getErrors()).containsExactly(entry("email", "error"));
//		}
//
//		@Test
//		void checkUniqueConstraint_PhoneNumberRegistered() {
//			UserRegistrationResult result = new UserRegistrationResult();
//			when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.empty());
//			when(messageSource.getMessage("registration.phoneNumber.duplicate", null, null)).thenReturn("error");
//			when(userMapper.findPhoneNumber(form.getPhoneNumber())).thenReturn(Optional.of(form.getPhoneNumber()));
//
//			boolean isUnique = mailVerificationService.checkUniqueConstraint(form, result);
//
//			assertThat(isUnique).isFalse();
//			assertThat(result.getErrors()).containsExactly(entry("phoneNumber", "error"));
//		}
//	}
}
