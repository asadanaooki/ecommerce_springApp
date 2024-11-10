INSERT INTO `user` (
    user_id, email, password, first_name_kanji, last_name_kanji, 
    first_name_kana, last_name_kana, gender, birth_date, post_code, 
    prefecture_id, address1, address2, phone_number, role
) VALUES (
    '550e8400-e29b-41d4-a716-446655440000', -- UUID.randomUUID().toString()で生成されたUUIDの例
    'example@example.com',
    '$2b$12$B9p1FTSq4.i9pzIomBMxV.EFa805FMSmzapQAleimNG3Rof0hhXH.', -- パスワードは"pass1234"
    '田中', '太郎',
    'タナカ', 'タロウ',
    'M', '1990-01-01',
    '1234567', 
    'b2b5c1773047b8ec73e8fdd70b30e090c50ec83b78fc90ad1f248bb58617', -- 北海道 
    '新宿区西新宿', -- 住所1（市区町村および番地）
    'マンション名101号室', -- 住所2（建物名・部屋番号など）
    '08012345678',
    '1'
);

INSERT INTO prefecture (prefecture_id, prefecture_name, display_order) VALUES
-- 北海道地方
('b2b5c1773047b8ec73e8fdd70b30e090c50ec83b78fc90ad1f248bb58617', '北海道', 1),

-- 東北地方
('9a65a31263df283945df02690536cfec0c4cf594760052af6924dd55933e', '青森県', 2),
('357ecaef50a3df13173bf0db7b654ce86ea2dab89b4082e869e78aed9d37', '岩手県', 3),
('5b47dd168f30820bad054332999ff394c3ece644323e5604829fc125b6e5', '秋田県', 4),
('24035e548a68fa105bae5aec595152e5e2be6f3b2d529c830f45aed6a11e', '山形県', 5),
('330c08e0336ae87add061f07728f6b0ff8da385a9d76c9c04da1a9465d05', '福島県', 6),
('d15ab0f3c2521b27a8c3f21d046a647795e5c5c33d5d3f4887fd0317c66e', '宮城県', 7),

-- 関東地方
('73d4f30b5374cb3b5026f91d707d493e341faadc6538b086d88d6c807b88', '茨城県', 8),
('2b1983aad8d6c2c4aceeeffc6829f882b44fbd5ae7d45da61be808422145', '栃木県', 9),
('43709ebd27ab35a9ba1ea24d1000939faa7e2227ace12d5e2a92c3f96053', '群馬県', 10),
('b2dc0b8fcd4fa3c316d4eabad5f93edf7fd2c4c6a0f35990382cd604f2e6', '埼玉県', 11),
('f0c18bfd7433fd6322e26c0c49e0816025a8c615bb0d4febb980243c446e', '千葉県', 12),
('730e1ddd782050cc63655ee66344bc049efdf794a6c97ef60de82923c104', '神奈川県', 13),
('835fa43c44ada31e5088207c176d5dbeb4a24d7ac138ec9bb5bfc356c3a4', '東京都', 14),

-- 中部地方
('8584fa5a371d0d0227c7ebbf6116ceee9d9d6465d71dc5281186733212b8', '山梨県', 15),
('9d0a36ec14c03151fc319957723cde4526ea48ad05f64d85ae558c90cb54', '新潟県', 16),
('35800b6e160687f03a3c4ae22fe00d1fe38e1b9350e622d6667fca3c8e8b', '福井県', 17),
('9641ab05ef7b681928d1b5ee5acd4940469fdd4b62bd62b2c2c83414a71d', '静岡県', 18),
('6560ab4ee0256e63d520d48890ad388d851784981c46c233713656412632', '長野県', 19),
('3865e2739b29ff4f83b6c9247988c9c30898f4c9a837688c44e2e637bf52', '富山県', 20),
('ef84cf204ee0b5e9364fc3b12d51a267b3e4a971c9c9ce2ecb70e6f9c8cc', '石川県', 21),
('9f6095379827c825a2859028c300547cab5c7071a04d2c0af2517594409a', '愛知県', 22),
('55cd92996dee0abbf5554ef049190236098c0286a1ce221f734b051cffd5', '岐阜県', 23),

-- 近畿地方
('2c810f3c9fb7c9eeaf0767a605845cc769bb32c27ce3b97851f034c1dc47', '滋賀県', 24),
('6bf8171851afa331b34154e674f55d10c5fd997345d5816db6c2c0de80dc', '大阪府', 25),
('78dafd6c26571d111ff351a4085b333032b37e581de433113a3e304a765a', '兵庫県', 26),
('a4ef53663b96e072d68d8573e3a7831a917184fab46b8855d51d61761e05', '奈良県', 27),
('a4178f0fadd4e17aa293b3bf4965e5ac9a39125c55c0dc511d8165322861', '和歌山県', 28),
('dd11cfd428ca66b65e74690b94065522562136e5fc3c49e493945bc6fedf', '京都府', 29),
('1fe2d6baf49caf632c3ca6d7694751931fb9140e1406cd1cc6c6df185646', '三重県', 30),

-- 中国地方
('04c7438ee4101a8ab24a3a8562f5462095b07d5a95bc58371972af2fb932', '山口県', 31),
('82a0db2ac626946896028ac8ae4802384dd38f343dc1a1574e8cd8bdf252', '広島県', 32),
('df8554520f9e3f970c2c74071c006b3d60ff9a3895610dd25ab9cdf15079', '岡山県', 33),
('e53bf335d55be2444d4bdce9192823e95d1e822ac6d1822d8dd707810dee', '島根県', 34),
('f3370f4179e27f8ccc80582523d82dbdfa98c91e2c57e06347d627915c18', '鳥取県', 35),

-- 四国地方
('74093101293398a670e48b1daabc3a3daeae3ca874f2128ff95a29c1f030', '愛媛県', 36),
('b6015c8dc177957e1a32191f2f9f4df2c47e30abb6937a4653f28345a64d', '香川県', 37),
('acdeda766d4d8a82f38d21ea731c8e6b9389af44b540612a6a44822cae71', '高知県', 38),
('06197c7c6bdd84f4a87795f781a8f3613271e8e6e925bda97eaf900294e3', '徳島県', 39),

-- 九州地方
('52036a267bcdb56b83e8b25400ffc4e2e135a9aaa6f21b3e12331215e98b', '福岡県', 40),
('8e92ec01ca14ed3639b90fb224c5155672d4c21cce27db955f546c4883d8', '大分県', 41),
('d5a3eac64dd4091e224f992bbde162a5d2c83fabee95370b00cb631b0229', '長崎県', 42),
('af9d0f9ec021da0b6794c84d8da4aa0e38c376f966ec0c07e4dfbd417908', '宮崎県', 43),
('019a8a6487078ed270e836ca1c2c0c4036b5f06baee0bb62c84f7cae327e', '熊本県', 44),
('0313db882ca082134ba7303a28a6826238695224f804537c0878f0f58ba0', '鹿児島県', 45),
('30eaed8b1d438e8744c66d56bf0b4e73db973ba76a445ebc29799c71aadc', '佐賀県', 46),

-- 沖縄地方
('836c7c1cbff5ce7cb0509c7a03c922fc25caee48b433fa38cf04db89b64f', '沖縄県', 47);


