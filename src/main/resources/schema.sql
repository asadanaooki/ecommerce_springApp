-- User Table
CREATE TABLE `user` (
    user_id CHAR(36) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password CHAR(60) NOT NULL,
    first_name_kanji VARCHAR(20) NOT NULL,
    last_name_kanji VARCHAR(20) NOT NULL,
    first_name_kana VARCHAR(50) NOT NULL,
    last_name_kana VARCHAR(50) NOT NULL,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
    birth_date DATE NOT NULL,
    post_code VARCHAR(10) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    role VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);
