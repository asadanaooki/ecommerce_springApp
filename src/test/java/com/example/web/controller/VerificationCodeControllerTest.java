package com.example.web.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Stream;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.example.service.user.VerificationCodeService;
import com.example.web.form.VerificationCodeForm;

@WebMvcTest(VerificationCodeController.class)
@WithMockUser
class VerificationCodeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messageSource;

    @MockBean
    VerificationCodeService verificationCodeService;

    @Test
    void showVerificationCodeForm() throws Exception {
        mockMvc.perform(get("/user/verify-code"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/verificationCodeInput"))
                .andExpect(model().attributeExists("form"));
    }
    
    @Test
    void showRegistrationComplete() throws Exception {
        mockMvc.perform(get("/user/registrationComplete"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/registrationComplete"));
    }

    @Nested
    class verifyInputCode {
        String inputCode = "123456";

        @Test
        void verifyInputCode_Success() throws Exception {
            mockMvc.perform(post("/user/verify-code").with(csrf())
                    .cookie(new Cookie("userId", "test"))
                    .param("code", inputCode))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/user/registrationComplete"))
                    .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("userId=; Max-Age=0")));

            verify(verificationCodeService).processUserRegistration(eq("test"), any(VerificationCodeForm.class));

        }

        @ParameterizedTest
        @MethodSource("provideInvalidVerificationCodeData")
        void verifyInputCode_ValidationError(String inputCode, List<String> expectedMessageKeys) throws Exception {

            ResultActions actions = mockMvc.perform(post("/user/verify-code").with(csrf())
                    .cookie(new Cookie("userId", "test"))
                    .param("code", inputCode));

            ModelAndView mv =actions.andReturn().getModelAndView();
            BindingResult result = 
                    (BindingResult) mv.getModel().get(BindingResult.MODEL_KEY_PREFIX + "form");
            
            List<String> expectedMessages = expectedMessageKeys.stream()
                    .map(key -> messageSource.getMessage(key, null,null)).toList();
            
            List<String> acutualMessages = result.getFieldErrors("code").stream()
                    .map(error -> error.getDefaultMessage()).toList();
            
            assertThat(acutualMessages).containsExactlyInAnyOrderElementsOf(acutualMessages);

            actions.andExpect(status().isOk())
            .andExpect(model().attributeExists("form"));
            

        }

        static Stream<Arguments> provideInvalidVerificationCodeData() {
            return Stream.of(
                    // 空文字 -> @NotBlank違反で"input.required"メッセージが期待される
                    Arguments.of("", List.of("input.required", "numeric.format")),
                    // 数字以外が含まれる -> @Digits違反で"numeric.format"メッセージが期待される
                    Arguments.of("abc", List.of("numeric.format")),
                    // 桁数オーバー(7桁) -> @Digits(integer=6)違反で"numeric.format"
                    Arguments.of("1234567", List.of("numeric.format")));
        }

    }

}
