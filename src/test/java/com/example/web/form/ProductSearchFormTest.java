package com.example.web.form;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductSearchFormTest {

    Validator validator;

    ProductSearchForm form;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        form = new ProductSearchForm();
        form.setPage("2");
        form.setKeyword("apple");
        form.setCategory("food");
        form.setPriceMin("100");
        form.setPriceMax("3000");
        form.setRating("4.5");
        form.setSort("NEW");

    }
    
    // 以下TODO:

    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @NotBlank 用のケース
            // ----------------------------------------------------
            // null
            "null, false",
            // ----------------------------------------------------
            // @Length(max=9) 用のケース
            // ----------------------------------------------------
            // 9文字
            "123456789,true",
            // 10文字
            "1234567890,false",
            // ----------------------------------------------------
            // @Pattern 用のケース (1以上の整数 または 小数(小数点以下すべて0))

            // ----------------------------------------------------
            // 整数951
            "951,true",
            // 整数01
            "01,false",
            // 小数4.00
            "4.00,true",
            // 小数3.8
            "3.8,false",
            // aあえ
            "aあえ,false",

    }, nullValues = "null")
    void validPage(String input, boolean expected) {
        // arrange
        form.setPage(input);

        // act & assert
        performValidation(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @Length(max=100) 用のケース
            // ----------------------------------------------------
            // 100文字
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,true",
            // 101文字
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,false"
    })
    void validKeyword(String input, boolean expected) {
        // arrange
        form.setKeyword(input);

        // act & assert
        performValidation(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @Length(max=50) 用のケース
            // ----------------------------------------------------
            // 50文字
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,true",
            // 51文字
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,false"
    })
    void validCategory(String input, boolean expected) {
        // arrange
        form.setCategory(input);

        // act & assert
        performValidation(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @NotBlank 用のケース
            // ----------------------------------------------------
            // 空白
            "'', false",
            // ----------------------------------------------------
            // @Length(max=9) 用のケース
            // ----------------------------------------------------
            // 9桁
            "123456789, true",
            // 10桁
            "1234567890, false",
            // ----------------------------------------------------
            // @@StringIntegerRange 用のケース
            // ----------------------------------------------------
            // 1000
            "1000,true",
            // -20
            "-20,false",
    })
    void validPriceMin(String input, boolean expected) {
        // arrange
        form.setPriceMin(input);

        // act & assert
        performValidation(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @NotBlank 用のケース
            // ----------------------------------------------------
            // 空白
            "'', false",
            // ----------------------------------------------------
            // @Length(max=9) 用のケース
            // ----------------------------------------------------
            // 9桁
            "123456789, true",
            // 10桁
            "1234567890, false",
            // ----------------------------------------------------
            // @@StringIntegerRange 用のケース
            // ----------------------------------------------------
            // 1000
            "1000,true",
            // -20
            "-20,false",

    })
    void validPriceMax(String input, boolean expected) {
        // arrange
        form.setPriceMax(input);

        // act & assert
        performValidation(expected);
    }
    
    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @NotBlank 用のケース
            // ----------------------------------------------------
            // null
            ", false",
            // ----------------------------------------------------
            // @Length(max=13) 用のケース
            // ----------------------------------------------------
            // 13桁
            "4.00000000000, true",
            // 14桁
            "12345678901234, false",
            // ----------------------------------------------------
            // @StringDecimalRange 用のケース
            // ----------------------------------------------------
            // 4.35
            "4.35,true",
            // 6.0
            "6.0,false",

    })
    void validRating(String input, boolean expected) {
        // arrange
        form.setRating(input);

        // act & assert
        performValidation(expected);
    }
    
    @ParameterizedTest
    @CsvSource(value = {
            // ----------------------------------------------------
            // @ValidSortKey 用のケース
            // ----------------------------------------------------
            // NEW
            "NEW, true",
            // INVALID
            "INVALID, false",

    })
    void validSortKey(String input, boolean expected) {
        // arrange
        form.setSort(input);

        // act & assert
        performValidation(expected);
    }

    private void performValidation(boolean expected) {
        Set<ConstraintViolation<ProductSearchForm>> violations = validator.validate(form);

        boolean actual = violations.isEmpty();
        assertThat(actual).isEqualTo(expected);
    }

}
