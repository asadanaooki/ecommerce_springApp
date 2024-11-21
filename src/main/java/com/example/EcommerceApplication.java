package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * EcommerceApplication クラスは、Spring Boot アプリケーションのエントリーポイントです。 アプリケーションを起動するための
 * main メソッドを提供します。
 */
@SpringBootApplication
@EnableCaching
public class EcommerceApplication {

	/**
	 * アプリケーションのメインメソッドです。 SpringApplication.run() を呼び出して、Spring Boot
	 * アプリケーションを起動します。
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
