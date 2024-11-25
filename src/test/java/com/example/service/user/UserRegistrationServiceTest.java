package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.enums.Gender;
import com.example.web.form.RegistrationForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserRegistrationService userRegistrationService;

    RegistrationForm form;

    @Mock
    MessageSource messageSource;

    @Mock
    StringRedisTemplate template;

    @Mock
    HashOperations<String, Object, Object> hashOperations;

    @Mock
    ObjectMapper objectMapper;

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

    static Stream<Arguments> provideUniqueCheckData() {
        return Stream.of(
                Arguments.of("全てユニーク", "test@example.com", "09012345678", true, true,
                        Collections.emptyList()),
                Arguments.of("eメールが重複", "test@example.com", "09012345678", false, true,
                        List.of("email")),
                Arguments.of("電話番号が重複", "test@example.com", "09012345678", true, false,
                        List.of("phoneNumber")),
                Arguments.of("全て重複", "test@example.com", "09012345678", false, false,
                        List.of("email", "phoneNumber")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideUniqueCheckData")
    void checkUniqueConstraint(String pattern, String email, String phoneNumber, boolean isEmailUnique,
            boolean isPhoneNumberUnique, List<String> errorFields) {
        when(userMapper.emailNotExists(email)).thenReturn(isEmailUnique);
        when(userMapper.phoneNumberNotExists(phoneNumber)).thenReturn(isPhoneNumberUnique);

        // 以下だとフォーマットしても戻らない
        errorFields.stream().forEach(field -> when(messageSource.getMessage(field + ".duplicate",
                null, null)));
        // 以下のようにしたい
        //        errorFields.stream().forEach(field ->
        //        when(messageSource.getMessage(field + ".duplicate", null, null)));

    }

    //    @Nested
    //    class checkUniqueConstraint {
    //
    //        @Test
    //        void checkUniqueConstraint_AllUnique() {
    //            UserRegistrationResult result = new UserRegistrationResult();
    //            when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.empty());
    //            when(userMapper.findPhoneNumber(form.getPhoneNumber())).thenReturn(Optional.empty());
    //
    //            boolean isUnique = userRegistrationService.checkUniqueConstraint(form, result);
    //
    //            assertThat(isUnique).isTrue();
    //            assertThat(result.getErrors()).isEmpty();
    //
    //        }
    //
    //        @Test
    //        void checkUniqueConstraint_EmailExists() {
    //            UserRegistrationResult result = new UserRegistrationResult();
    //            when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.of(form.getEmail()));
    //            when(messageSource.getMessage("registration.email.duplicate", null, null)).thenReturn("error");
    //
    //            boolean isUnique = userRegistrationService.checkUniqueConstraint(form, result);
    //
    //            assertThat(isUnique).isFalse();
    //            assertThat(result.getErrors()).containsExactly(entry("email", "error"));
    //        }
    //
    //        @Test
    //        void checkUniqueConstraint_PhoneNumberExists() {
    //            UserRegistrationResult result = new UserRegistrationResult();
    //            when(userMapper.findEmail(form.getEmail())).thenReturn(Optional.empty());
    //            when(userMapper.findPhoneNumber(form.getPhoneNumber())).thenReturn(Optional.of(form.getPhoneNumber()));
    //            when(messageSource.getMessage("registration.phoneNumber.duplicate", null, null)).thenReturn("error");
    //
    //            boolean isUnique = userRegistrationService.checkUniqueConstraint(form, result);
    //
    //            assertThat(isUnique).isFalse();
    //            assertThat(result.getErrors()).containsExactly(entry("phoneNumber", "error"));
    //        }
    //
            @Nested
            class saveTempRegistrationInfo {
                Map<String, Object> map = new HashMap<String, Object>();
    
                @BeforeEach
                void setup() {
                    map.put("email", form.getEmail());
                    map.put("password", form.getPassword());
                    map.put("lastNameKanji", form.getLastNameKanji());
                    map.put("firstNameKanji", form.getFirstNameKanji());
                    map.put("lastNameKana", form.getLastNameKana());
                    map.put("firstNameKana", form.getFirstNameKana());
                    map.put("gender", form.getGender());
                    map.put("birthDate", form.getBirthDate());
                    map.put("postCode", form.getPostCode());
                    map.put("prefectureId", form.getPrefectureId());
                    map.put("address1", form.getAddress1());
                    map.put("address2", form.getAddress2());
                    map.put("phoneNumber", form.getPhoneNumber());
                }
    
                @SuppressWarnings("unchecked")
                @Test
                void saveTempRegistrationInfo_Success() {
    
                    UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
                    try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class);
                            // TODO どこで改行するか
                            MockedConstruction<Random> mockedRandom = mockConstruction(Random.class,
                                    (mock, context) -> {
                                        when(mock.nextInt(999999 + 1)).thenReturn(123456);
                                    })) {
                        when(objectMapper.convertValue(eq(form), any(TypeReference.class))).thenReturn(map);
    
                        when(template.opsForHash()).thenReturn(hashOperations);
                        mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
                        String userId = uuid.toString();
                        userRegistrationService.expirationTimeMinutes = 10;
    
                        Pair<String, String> result = userRegistrationService.saveTempRegistrationInfo(form);
    
                        assertThat(result.getFirst()).isEqualTo(userId);
                        assertThat(result.getSecond()).isEqualTo("123456");
                        verify(hashOperations).putAll("mail_verification:" + userId, map);
                        verify(template).expire("mail_verification:" + userId, 10, TimeUnit.MINUTES);
                    }
                }
    
            }
        }

    @Nested
    class registerTempUser {
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

        //		@Test
        //		void registerTempUser_NotUnique() {
        //			when(mailVerificationService.checkUniqueConstraint(eq(form), any(UserRegistrationResult.class)))
        //			.thenReturn(false);
        //
        //			UserRegistrationResult result = userService.registerTempUser(form);
        //
        //			assertThat(result.isSuccess()).isFalse();
        //		}

    }

}
