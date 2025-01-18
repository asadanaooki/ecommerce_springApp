package com.example.validation.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SortKeyValidatorTest {
     SortKeyValidator validator;
     
     @BeforeEach
     void setup() {
         validator = new SortKeyValidator();
     }
     
     @ParameterizedTest
     @MethodSource("provideSortKeyData")
     void checkSortKeyValidator(String input, boolean expected) {
         // act
         boolean actual = validator.isValid(input, null);
         
         // assert
         assertThat(actual).isEqualTo(expected);
     }
     

      static Stream<Arguments> provideSortKeyData() {
         return Stream.of(
             // 正常系 (true)
             Arguments.of("NEW", true),
             Arguments.of("PRICE_DESC", true),
             Arguments.of("PRICE_ASC", true),
             Arguments.of("RATING", true),

             // 異常系 (false)
             Arguments.of("new", false),
             Arguments.of(null, false)
         );
     }

}
