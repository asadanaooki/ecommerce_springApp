package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.example.domain.model.enums.Gender;
import com.example.web.form.RegistrationForm;

@SpringBootTest
class MailVerificationServiceTest {

	@MockBean
	StringRedisTemplate template;

	@Mock
	HashOperations<String, Object, Object> hashOperations;

	@Autowired
	MailVerificationService mailVerificationService;

	MockedConstruction<Random> mockedRandom;

	@BeforeEach
	void setUp() {
		mockedRandom = mockConstruction(Random.class, (mock, context) -> when(mock.nextInt(anyInt())).thenReturn(50));
	}

	@AfterEach
	void tearDown() {
		mockedRandom.close();
	}

	@Test
	void generateVerificationCode_Success() {

		Optional<String> result = mailVerificationService.generateVerificationCode();

		assertThat(result.get()).isEqualTo("mail_verification:100050");
		verify(template, times(1)).hasKey("mail_verification:100050");
	}

	@Test
	void generateVerificationCode_Success99Attempt() {

		AtomicInteger counter = new AtomicInteger();

		when(template.hasKey("mail_verification:100050")).thenAnswer(inv -> counter.incrementAndGet() < 99);

		Optional<String> result = mailVerificationService.generateVerificationCode();

		assertThat(result.get()).isEqualTo("mail_verification:100050");
		verify(template, times(99)).hasKey("mail_verification:100050");
	}

	@Test
	void generateVerificationCode_Success100Attempt() {
		AtomicInteger counter = new AtomicInteger();

		when(template.hasKey("mail_verification:100050")).thenAnswer(inv -> counter.incrementAndGet() < 100);

		Optional<String> result = mailVerificationService.generateVerificationCode();

		assertThat(result.get()).isEqualTo("mail_verification:100050");
		verify(template, times(100)).hasKey("mail_verification:100050");
	}

	@Test
	void generateVerificationCode_FailAfterMaxAttempt() {
		AtomicInteger counter = new AtomicInteger();

		when(template.hasKey("mail_verification:100050")).thenAnswer(inv -> counter.incrementAndGet() <= 100);

		Optional<String> result = mailVerificationService.generateVerificationCode();

		assertThat(result).isEmpty();
		verify(template, times(100)).hasKey("mail_verification:100050");
	}

	@Test
	void generateVerificationCode_FailAfter150Attempt() {
		AtomicInteger counter = new AtomicInteger();

		when(template.hasKey("mail_verification:100050")).thenAnswer(inv -> counter.incrementAndGet() < 150);

		Optional<String> result = mailVerificationService.generateVerificationCode();

		assertThat(result).isEmpty();
		verify(template, times(100)).hasKey("mail_verification:100050");
	}

	@Test
	void saveTempRegistrationInfo() {
		RegistrationForm form = new RegistrationForm();
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

		String key = "mail_verification:100050";

		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
		when(template.opsForHash()).thenReturn(hashOperations);


		mailVerificationService.saveTempRegistrationInfo(form, key);

		verify(template.opsForHash()).putAll(eq(key), captor.capture());
		verify(template).expire(key, MailVerificationService.expirationTimeMinutes, TimeUnit.MINUTES);
		Map<String, String> capMap = captor.getValue();

		assertThat(capMap.get("email")).isEqualTo("test@example.com");
		assertThat(capMap.get("password")).isEqualTo("password123");
		assertThat(capMap.get("lastNameKanji")).isEqualTo("山田");
		assertThat(capMap.get("firstNameKanji")).isEqualTo("太郎");
		assertThat(capMap.get("lastNameKana")).isEqualTo("ヤマダ");
		assertThat(capMap.get("firstNameKana")).isEqualTo("タロウ");
		assertThat(capMap.get("gender")).isEqualTo(Gender.MALE.toString());
		assertThat(capMap.get("birthDate")).isEqualTo(LocalDate.of(1990, 1, 1).toString());
		assertThat(capMap.get("postCode")).isEqualTo("1234567");
		assertThat(capMap.get("prefectureId")).isEqualTo("01");
		assertThat(capMap.get("address1")).isEqualTo("Shibuya-ku");
		assertThat(capMap.get("address2")).isEqualTo("Building 101");
		assertThat(capMap.get("phoneNumber")).isEqualTo("09012345678");
	}
}
