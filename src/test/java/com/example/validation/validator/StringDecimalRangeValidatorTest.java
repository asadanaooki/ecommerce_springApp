package com.example.validation.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.example.validation.annotation.StringDecimalRange;


class StringDecimalRangeValidatorTest {

    StringDecimalRangeValidator validator;

    @BeforeEach
    void setup() {
        StringDecimalRange annotation = Mockito.mock(StringDecimalRange.class);
        Mockito.when(annotation.min()).thenReturn(0.0);
        Mockito.when(annotation.max()).thenReturn(5.0);
        
        validator = new StringDecimalRangeValidator();
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @MethodSource("provideValidatorData")
    void validStringDecimalRange(String input, boolean expected) {
        // act
        boolean actual = validator.isValid(input, null);

        // assert
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> provideValidatorData() {
        return Stream.of(
                // --- 正常系 (範囲内) ---
                Arguments.of("0.0", true),
                Arguments.of("2.5", true),
                Arguments.of("5", true),
                Arguments.of("+4", true),

                // --- 異常系 (範囲外 / 数値変換不可) ---
                Arguments.of("-1", false),
                Arguments.of("5.1", false),
                Arguments.of("abc", false),
                Arguments.of(" ", false),
                Arguments.of(null, false));
    }

}
