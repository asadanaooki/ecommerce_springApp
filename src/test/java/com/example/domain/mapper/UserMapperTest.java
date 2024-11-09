package com.example.domain.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.domain.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserMapperTest extends MapperTestBase {
	
	
	UserMapperTest() {
		super(TEST_DATA_FILE_PATH);
	}

	static final String TEST_DATA_FILE_PATH = "data/userMapper/init_data.xml";
	final String EXISTING_EMAIL = "testuser@example.com";
	final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
	final String EXPECTED_USER_ID = "11111111-1111-1111-1111-111111111111";
	final String EXPECTED_PASSWORD = "$2b$12$RXeMJPTgWvZ.ACZl3Qo5yev1zvcOQPnIrVs.4Ta9x2ge2.xCgcYWm";
	final String EXPECTED_FIRST_NAME_KANJI = "山田";
	final String EXPECTED_LAST_NAME_KANJI = "太郎";
	final String EXPECTED_FIRST_NAME_KANA = "ヤマダ";
	final String EXPECTED_LAST_NAME_KANA = "タロウ";
	final String EXPECTED_GENDER = "F";
	final String EXPECTED_BIRTH_DATE = "2000-01-01";
	final String EXPECTED_POST_CODE = "000-0000";
	final String EXPECTED_ADDRESS = "テスト県テスト市テスト町1-1-1";
	final String EXPECTED_PHONE_NUMBER = "09000000000";

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
