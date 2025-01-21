package com.example.util.Security;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {

    SecurityUtil securityUtil;

    SecurityContext context;

    @BeforeEach
    void setup() {
        securityUtil = new SecurityUtil();

        // テスト開始時点で「未ログイン（匿名認証）」状態にしておく
        context = SecurityContextHolder.getContext();

        AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("key",
                "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        context.setAuthentication(anonymousAuthenticationToken);
        SecurityContextHolder.setContext(context);
    }

    @Nested
    class isLogin {
        @Test
        void isLogin_WhenNotLogin() {
            // act
            boolean result = securityUtil.isLogin();

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void isLogin_WhenLogin() {
            // arrange
            prepareLoginUser();

            // act
            boolean result = securityUtil.isLogin();

            // assert
            assertThat(result).isTrue();
        }

        @AfterEach
        void tearDown() {
            SecurityContextHolder.clearContext();
        }
    }

    @Nested
    class getLoginUserId {
        @Test
        void getLoginUserId_whenNotLogin() {
            // act
            Optional<String> result = securityUtil.getLoginUserId();

            // assert
            assertThat(result).isEmpty();
        }

        @Test
        void getLoginUserId_whenLogin() {
             // arrange
            prepareLoginUser();

            // act
            Optional<String> result = securityUtil.getLoginUserId();

            // assert
            assertThat(result.get()).isEqualTo("testUser");
        }
    }

    private void prepareLoginUser() {
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser",
                "password", AuthorityUtils.createAuthorityList("ROLE_USER"));
        context.setAuthentication(auth);
    }
}
