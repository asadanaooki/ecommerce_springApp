INSERT INTO `user` (
    user_id, email, password, first_name_kanji, last_name_kanji, 
    first_name_kana, last_name_kana, gender, birth_date, post_code, 
    address, phone_number, role
) VALUES (
    '550e8400-e29b-41d4-a716-446655440000', -- UUID.randomUUID().toString()で生成されたUUIDの例
    'example@example.com',
    '$2b$12$B9p1FTSq4.i9pzIomBMxV.EFa805FMSmzapQAleimNG3Rof0hhXH.', -- パスワードは"pass1234"
    '田中', '太郎',
    'タナカ', 'タロウ',
    'M', '1990-01-01',
    '123-4567', '東京都新宿区',
    '08012345678',
    '1'
);