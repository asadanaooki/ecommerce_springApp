package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.example.constant.UserConstant;
import com.example.domain.mapper.UserMapper;
import com.example.domain.model.entity.User;
import com.example.exception.BusinessException;
import com.example.service.user.converter.UserConverter;
import com.example.web.form.VerificationCodeForm;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceTest {

    @Mock
    StringRedisTemplate template;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    VerificationCodeService verificationCodeService;

    @Mock
    HashOperations<String, String, String> hashOperations;

    @Mock
    MessageSource messageSource;

    @Mock
    ValueOperations<String, String> valueOperations;

    VerificationCodeForm form;

    String userId = "123e4567-e89b-12d3-a456-426614174000";

    String key;

    String inputCode = "123456";

    @Nested
    class processUserRegistration {
        @BeforeEach
        void setup() {
            form = new VerificationCodeForm(inputCode);
            key = UserConstant.MAIL_VERIFICATION_PREFIX + userId;
        }

        @Test
        void processUserRegistration_Success() {
            when(template.hasKey(key)).thenReturn(true);
            when(template.<String, String> opsForHash()).thenReturn(hashOperations);
            when(hashOperations.entries(key)).thenReturn(Map.of("code", "123456"));

            try (MockedStatic<UserConverter> mockedConverter = mockStatic(UserConverter.class)) {
                mockedConverter.when(() -> UserConverter.toEntity(anyMap(),any())).thenReturn(new User());

                doNothing().when(userMapper).registerUser(any());

                verificationCodeService.processUserRegistration(userId, form);

                verify(userMapper).registerUser(any());
                verify(template).delete(key);
            }
        }

        @Test
        void processUserRegistration_UserNotFound() {
            when(template.hasKey(key)).thenReturn(false);

            assertThatExceptionOfType(BusinessException.class)
                    .isThrownBy(() -> verificationCodeService.processUserRegistration(userId, form))
                    .extracting(BusinessException::getMessageKey, BusinessException::getViewName)
                    .containsExactly("verificationCode.expired", UserConstant.VIEW_NAME_VERIFICATION_CODE_INPUT);

        }

        @Test
        void processUserRegistration_MaxAttemptNotExceeded() {
            when(template.hasKey(key)).thenReturn(true);
            when(template.<String, String> opsForHash()).thenReturn(hashOperations);
            when(hashOperations.entries(key)).thenReturn(Map.of("code", "654321", "email", "test@example.com"));
            when(hashOperations.increment(key, "attempt", 1)).thenReturn(2L);

            assertThatExceptionOfType(BusinessException.class)
            .isThrownBy(() -> verificationCodeService.processUserRegistration(userId, form))
            .extracting(BusinessException::getMessageKey, BusinessException::getViewName)
            .containsExactly("registration.invalid", UserConstant.VIEW_NAME_VERIFICATION_CODE_INPUT);

        }

        @Test
        void processUserRegistration_ExceededMaxAttempt() {
            when(template.hasKey(key)).thenReturn(true);
            when(template.<String, String> opsForHash()).thenReturn(hashOperations);
            when(hashOperations.entries(key)).thenReturn(Map.of("code", "654321", "email", "test@example.com"));
            when(hashOperations.increment(key, "attempt", 1)).thenReturn(5L);
            when(template.opsForValue()).thenReturn(valueOperations);

            assertThatExceptionOfType(BusinessException.class)
            .isThrownBy(() -> verificationCodeService.processUserRegistration(userId, form))
            .extracting(BusinessException::getMessageKey, BusinessException::getViewName)
            .containsExactly("registration.locked", UserConstant.VIEW_NAME_VERIFICATION_CODE_INPUT);

        }
    }

}
