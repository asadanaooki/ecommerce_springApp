package com.example.validation.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.example.validation.annotation.StringIntegerRange;

class StringIntegerRangeValidatorTest {

    StringIntegerRangeValidator validator;

    @BeforeEach
    void setup() {
        StringIntegerRange annotation = Mockito.mock(StringIntegerRange.class);
        Mockito.when(annotation.min()).thenReturn(0);
        Mockito.when(annotation.max()).thenReturn(1000);
        
        validator = new StringIntegerRangeValidator();
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @MethodSource("provideValidatorData")
    void validStringIntegerRange(String input, boolean expected) {
        // act
        boolean actual = validator.isValid(input, null);

        // assert
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> provideValidatorData() {
        return Stream.of(
                // --- 正常系 (範囲内) ---
                Arguments.of("0", true),
                Arguments.of("525.0000", true),
                Arguments.of("1000", true),
                Arguments.of("+311", true),
                Arguments.of("0028", true),
                // --- 異常系 (範囲外 / 数値変換不可) ---
                Arguments.of("-1", false),
                Arguments.of("1001", false),
                Arguments.of("455.5", false),
                Arguments.of("abc", false),
                Arguments.of(null, false));
    }


}
