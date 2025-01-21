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

create table if not exists category(
category_id char(60) not null,
category_name varchar(100) not null,
primary key (category_id),
unique key (category_name)
);

create table if not exists product(
  product_id          CHAR(60)       NOT NULL,
  product_name        VARCHAR(100)   NOT NULL,
  price               INT            NOT NULL,
  product_description VARCHAR(1000)  NOT NULL,
  stock               INT            NOT NULL,
  category_id         CHAR(60)       NOT NULL,
  start_sale_date     DATE           NOT NULL,
  end_sale_date DATE not null default '9999-12-31',
  primary key(product_id),
  unique key(product_name),
  foreign key(category_id) references category(category_id)
);

CREATE TABLE IF NOT EXISTS review (
  product_id  CHAR(60)      NOT NULL,
  user_id     CHAR(36)      NOT NULL,
  rating      INT           NOT NULL,    -- 1～5 の評価スコア想定
  review_text VARCHAR(500)  NOT NULL,
  PRIMARY KEY (product_id, user_id),
  FOREIGN KEY (product_id) REFERENCES product(product_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
) ;
