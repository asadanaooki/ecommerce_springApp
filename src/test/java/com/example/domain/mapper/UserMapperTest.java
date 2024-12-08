package com.example.domain.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.domain.model.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserMapperTest extends MapperTestBase {

    UserMapperTest() {
        super(TEST_DATA_FILE_PATH);
    }

    static final String TEST_DATA_FILE_PATH = "data/userMapper/init_data.xml";
    final String EXISTING_EMAIL = "example2@example.com";
    final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
    final String EXPECTED_USER_ID = "550e8400-e29b-41d4-a716-446655440001";
    final String EXPECTED_PASSWORD = "$2b$12$B9p1FTSq4.i9pzIomBMxV.EFa805FMSmzapQAleimNG3Rof0hhXH.";
    static final String EXPECTED_FIRST_NAME_KANJI = "山田";
    static final String EXPECTED_LAST_NAME_KANJI = "太郎";
    static final String EXPECTED_FIRST_NAME_KANA = "ヤマダ";
    static final String EXPECTED_LAST_NAME_KANA = "タロウ";
    static final Gender EXPECTED_GENDER = Gender.FEMALE;
    static final LocalDate EXPECTED_BIRTH_DATE = LocalDate.of(2000, 1, 1);
    static final String EXPECTED_POST_CODE = "000-0000";
    static final String EXPECTED_PREFECTURE_ID = "13";
    static final String EXPECTED_ADDRESS1 = "テスト県テスト市テスト町1-1-1";
    static final String EXPECTED_ADDRESS2 = "テストビル101号室";
    static final String EXPECTED_PHONE_NUMBER = "0900000001";

    @Autowired
    UserMapper userMapper;

    @Test
    void findUserByEmail_found() {

        User foundUser = userMapper.findUserCredentialsByEmail(EXISTING_EMAIL).get();

        assertThat(foundUser.getUserId()).isEqualTo(EXPECTED_USER_ID);
        assertThat(foundUser.getPassword()).isEqualTo(EXPECTED_PASSWORD);

    }

    @Test
    void findUserByEmail_notFound() {
        Optional<User> foundUser = userMapper.findUserCredentialsByEmail(NON_EXISTENT_EMAIL);
        assertThat(foundUser).isEmpty();
    }

}
