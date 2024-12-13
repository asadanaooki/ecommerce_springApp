package com.example.domain.mapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.domain.model.enums.Gender;

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

//    @Test
//    void findUserByEmail_found() {
//
//        User foundUser = userMapper.findUserCredentialsByEmail(EXISTING_EMAIL).get();
//
//        assertThat(foundUser.getUserId()).isEqualTo(EXPECTED_USER_ID);
//        assertThat(foundUser.getPassword()).isEqualTo(EXPECTED_PASSWORD);
//
//    }
//
//    @Test
//    void findUserByEmail_notFound() {
//        Optional<User> foundUser = userMapper.findUserCredentialsByEmail(NON_EXISTENT_EMAIL);
//        assertThat(foundUser).isEmpty();
//    }

//    @Nested
//    class registerUser {
//        User user;
//
//        @BeforeEach
//        void setup() {
//            user = new User();
//            user.setUserId(EXPECTED_USER_ID);
//            user.setEmail(EXISTING_EMAIL);
//            user.setPassword(EXPECTED_PASSWORD);
//            user.setFirstNameKanji(EXPECTED_FIRST_NAME_KANJI);
//            user.setLastNameKanji(EXPECTED_LAST_NAME_KANJI);
//            user.setFirstNameKana(EXPECTED_FIRST_NAME_KANA);
//            user.setLastNameKana(EXPECTED_LAST_NAME_KANA);
//            user.setGender(Gender.FEMALE);
//            user.setBirthDate(EXPECTED_BIRTH_DATE);
//            user.setPostCode(EXPECTED_POST_CODE);
//            user.setPrefectureId(EXPECTED_PREFECTURE_ID);
//            user.setAddress1(EXPECTED_ADDRESS1);
//            user.setPhoneNumber(EXPECTED_PHONE_NUMBER);
//            user.setRole(UserRole.USER);
//        }
//
//        @Test
//        void registerUser_WithAddress2() throws Exception {
//            user.setAddress2(EXPECTED_ADDRESS2);
//
//            userMapper.registerUser(user);
//
//            String query = "SELECT * FROM \"user\" WHERE user_id = '" + EXPECTED_USER_ID + "'";
//            ITable table = geTable("User", query);
//
//            assertThat(table.getRowCount()).isEqualTo(1);
//
//            // 各フィールドの値を検証
//            assertThat(table.getValue(0, "user_id")).isEqualTo(EXPECTED_USER_ID);
//            assertThat(table.getValue(0, "email")).isEqualTo(EXISTING_EMAIL);
//            assertThat(table.getValue(0, "password")).isEqualTo(EXPECTED_PASSWORD);
//            assertThat(table.getValue(0, "first_name_kanji")).isEqualTo(EXPECTED_FIRST_NAME_KANJI);
//            assertThat(table.getValue(0, "last_name_kanji")).isEqualTo(EXPECTED_LAST_NAME_KANJI);
//            assertThat(table.getValue(0, "first_name_kana")).isEqualTo(EXPECTED_FIRST_NAME_KANA);
//            assertThat(table.getValue(0, "last_name_kana")).isEqualTo(EXPECTED_LAST_NAME_KANA);
//            assertThat(table.getValue(0, "gender")).isEqualTo(Gender.FEMALE.getCode());
//            assertThat(table.getValue(0, "birth_date").toString()).isEqualTo(EXPECTED_BIRTH_DATE.toString());
//            assertThat(table.getValue(0, "post_code")).isEqualTo(EXPECTED_POST_CODE);
//            assertThat(table.getValue(0, "prefecture_id")).isEqualTo(EXPECTED_PREFECTURE_ID);
//            assertThat(table.getValue(0, "address1")).isEqualTo(EXPECTED_ADDRESS1);
//            assertThat(table.getValue(0, "address2")).isEqualTo(EXPECTED_ADDRESS2);
//            assertThat(table.getValue(0, "phone_number")).isEqualTo(EXPECTED_PHONE_NUMBER);
//            assertThat(table.getValue(0, "role")).isEqualTo(UserRole.USER.getCode());
//        }
//        
//        @Test
//        void registerUser_WithoutAddress2() throws Exception {
//
//            userMapper.registerUser(user);
//
//            String query = "SELECT * FROM \"user\" WHERE user_id = '" + EXPECTED_USER_ID + "'";
//            ITable table = geTable("User", query);
//
//            assertThat(table.getRowCount()).isEqualTo(1);
//
//            // 各フィールドの値を検証
//            assertThat(table.getValue(0, "user_id")).isEqualTo(EXPECTED_USER_ID);
//            assertThat(table.getValue(0, "email")).isEqualTo(EXISTING_EMAIL);
//            assertThat(table.getValue(0, "password")).isEqualTo(EXPECTED_PASSWORD);
//            assertThat(table.getValue(0, "first_name_kanji")).isEqualTo(EXPECTED_FIRST_NAME_KANJI);
//            assertThat(table.getValue(0, "last_name_kanji")).isEqualTo(EXPECTED_LAST_NAME_KANJI);
//            assertThat(table.getValue(0, "first_name_kana")).isEqualTo(EXPECTED_FIRST_NAME_KANA);
//            assertThat(table.getValue(0, "last_name_kana")).isEqualTo(EXPECTED_LAST_NAME_KANA);
//            assertThat(table.getValue(0, "gender")).isEqualTo(Gender.FEMALE.getCode());
//            assertThat(table.getValue(0, "birth_date").toString()).isEqualTo(EXPECTED_BIRTH_DATE.toString());
//            assertThat(table.getValue(0, "post_code")).isEqualTo(EXPECTED_POST_CODE);
//            assertThat(table.getValue(0, "prefecture_id")).isEqualTo(EXPECTED_PREFECTURE_ID);
//            assertThat(table.getValue(0, "address1")).isEqualTo(EXPECTED_ADDRESS1);
//            assertThat(table.getValue(0, "phone_number")).isEqualTo(EXPECTED_PHONE_NUMBER);
//            assertThat(table.getValue(0, "role")).isEqualTo(UserRole.USER.getCode());
//        }
//    }

}
