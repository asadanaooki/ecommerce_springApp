-- User Table
CREATE TABLE `user` (
    user_id CHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password CHAR(60) NOT NULL,
    first_name_kanji VARCHAR(20) NOT NULL,
    last_name_kanji VARCHAR(20) NOT NULL,
    first_name_kana VARCHAR(50) NOT NULL,
    last_name_kana VARCHAR(50) NOT NULL,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
    birth_date DATE NOT NULL,
    post_code VARCHAR(10) NOT NULL,
    prefecture_id CHAR(60) NOT NULL,
    address1 VARCHAR(255) NOT NULL,
    address2 VARCHAR(255),
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    role CHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_prefecture FOREIGN KEY (prefecture_id)
    REFERENCES prefecture(prefecture_id)
);

--都道府県テーブル
CREATE TABLE prefecture (
    prefecture_id CHAR(60) PRIMARY KEY,
    prefecture_name VARCHAR(50) NOT NULL,
    display_order INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


