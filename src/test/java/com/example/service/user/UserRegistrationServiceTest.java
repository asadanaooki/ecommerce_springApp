package com.example.service.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

//    @Mock
//    UserMapper userMapper;
//
//    @Spy
//    @InjectMocks
//    UserRegistrationService userRegistrationService;
//
//    RegistrationForm form;
//
//    @Mock
//    MessageSource messageSource;
//
//    @Mock
//    StringRedisTemplate template;
//
//    @Mock
//    HashOperations<String, Object, Object> hashOperations;
//
//    @Mock
//    ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        form = new RegistrationForm();
//        form.setEmail("test@example.com");
//        form.setPassword("password123");
//        form.setLastNameKanji("山田");
//        form.setFirstNameKanji("太郎");
//        form.setLastNameKana("ヤマダ");
//        form.setFirstNameKana("タロウ");
//        form.setGender(Gender.MALE);
//        form.setBirthDate(LocalDate.of(1990, 1, 1));
//        form.setPostCode("1234567");
//        form.setPrefectureId("01");
//        form.setAddress1("Shibuya-ku");
//        form.setAddress2("Building 101");
//        form.setPhoneNumber("09012345678");
//
//    }
//
//    static Stream<Arguments> provideUniqueCheckData() {
//        return Stream.of(
//                Arguments.of("全てユニーク", "test@example.com", "09012345678", true, true,
//                        Collections.emptyList(), true),
//                Arguments.of("eメールが重複", "test@example.com", "09012345678", false, true,
//                        List.of("email"), false),
//                Arguments.of("電話番号が重複", "test@example.com", "09012345678", true, false,
//                        List.of("phoneNumber"), false),
//                Arguments.of("全て重複", "test@example.com", "09012345678", false, false,
//                        List.of("email", "phoneNumber"), false));
//    }
//
//    @ParameterizedTest(name = "{0}")
//    @MethodSource("provideUniqueCheckData")
//    void checkUniqueConstraint(String pattern, String email, String phoneNumber, boolean emailUnique,
//            boolean phoneUnique, List<String> errorFields, boolean expected) {
//        when(userMapper.isEmailUnique(email)).thenReturn(emailUnique);
//        when(userMapper.isPhoneNumberUnique(phoneNumber)).thenReturn(phoneUnique);
//
//        errorFields.stream()
//                .forEach(field -> when(messageSource.getMessage(field + ".duplicate", null, null))
//                        .thenReturn("error"));
//
//        UserRegistrationResult result = new UserRegistrationResult();
//        boolean isUnique = userRegistrationService.checkUniqueConstraint(form, result);
//
//        assertThat(isUnique).isEqualTo(isUnique).isEqualTo(expected);
//
//        if (errorFields.isEmpty()) {
//            assertThat(result.getErrors()).isEmpty();
//        } else {
//            Map<String, String> expectedErrors = errorFields.stream().collect(Collectors.toMap(field -> field,
//                    field -> "error"));
//            assertThat(result.getErrors()).containsExactlyEntriesOf(expectedErrors);
//        }
//
//        @Nested
//        class saveTempRegistrationInfo {
//            Map<String, Object> map = new HashMap<String, Object>();
//
//            @BeforeEach
//            void setup() {
//                map.put("email", form.getEmail());
//                map.put("password", form.getPassword());
//                map.put("lastNameKanji", form.getLastNameKanji());
//                map.put("firstNameKanji", form.getFirstNameKanji());
//                map.put("lastNameKana", form.getLastNameKana());
//                map.put("firstNameKana", form.getFirstNameKana());
//                map.put("gender", form.getGender());
//                map.put("birthDate", form.getBirthDate());
//                map.put("postCode", form.getPostCode());
//                map.put("prefectureId", form.getPrefectureId());
//                map.put("address1", form.getAddress1());
//                map.put("address2", form.getAddress2());
//                map.put("phoneNumber", form.getPhoneNumber());
//            }
//
//            @SuppressWarnings("unchecked")
//            @Test
//            void saveTempRegistrationInfo_Success() {
//
//                UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//                try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class);
//                        // TODO どこで改行するか
//                        MockedConstruction<Random> mockedRandom = mockConstruction(Random.class,
//                                (mock, context) -> {
//                                    when(mock.nextInt(999999 + 1)).thenReturn(123456);
//                                })) {
//                    when(objectMapper.convertValue(eq(form), any(TypeReference.class))).thenReturn(map);
//
//                    when(template.opsForHash()).thenReturn(hashOperations);
//                    mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
//                    String userId = uuid.toString();
//                    userRegistrationService.expirationTimeMinutes = 10;
//
//                    Pair<String, String> result = userRegistrationService.saveTempRegistrationInfo(form);
//
//                    assertThat(result.getFirst()).isEqualTo(userId);
//                    assertThat(result.getSecond()).isEqualTo("123456");
//                    verify(hashOperations).putAll("mail_verification:" + userId, map);
//                    verify(template).expire("mail_verification:" + userId, 10, TimeUnit.MINUTES);
//                }
//            }
//
//        }
//    }
//
//    @Nested
//    class registerTempUser {
//        @Test
//        void registerTempUser_Success() {
//            when(template.hasKey("user_registration_lock:" + form.getEmail())).thenReturn(false);
//            doReturn(true).when(userRegistrationService).checkUniqueConstraint(eq(form),
//                    any(UserRegistrationResult.class));
//            doReturn(Pair.of("123e4567-e89b-12d3-a456-426614174000", "123456")).when(userRegistrationService)
//                    .saveTempRegistrationInfo(form);
//
//            UserRegistrationResult result = userRegistrationService.registerTempUser(form);
//
//            assertThat(result.isSuccess()).isTrue();
//            assertThat(result.getUserId()).isEqualTo("123e4567-e89b-12d3-a456-426614174000");
//        }
//
//        @Test
//        void registerTempUser_Locked() {
//            when(template.hasKey("user_registration_lock:" + form.getEmail())).thenReturn(true);
//            when(messageSource.getMessage("registration.locked", null, null)).thenReturn("error");
//
//            UserRegistrationResult result = userRegistrationService.registerTempUser(form);
//
//            assertThat(result.isSuccess()).isFalse();
//            assertThat(result.getErrors()).containsExactly(entry("global", "error"));
//        }
//
//        @Test
//        void registerTempUser_NotUnique() {
//            when(template.hasKey("user_registration_lock:" + form.getEmail())).thenReturn(false);
//            doReturn(false).when(userRegistrationService).checkUniqueConstraint(eq(form),
//                    any(UserRegistrationResult.class));
//
//            UserRegistrationResult result = userRegistrationService.registerTempUser(form);
//
//            assertThat(result.isSuccess()).isFalse();
//        }

   // }

}
