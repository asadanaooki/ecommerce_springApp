package com.example.converter.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.enums.SortKey;
import com.example.web.form.ProductSearchForm;

class ProductConverterTest {
    
    ProductSearchForm form;
    
    ProductSearchRequestDto expected;
    
    @BeforeEach
    void setup() {
        form = new ProductSearchForm();
        form.setPriceMin("1000");
        form.setPriceMax("2000");
        form.setRating("3.5");
        form.setCategory("testCategory");
        form.setKeyword("testKeyword");
        form.setPage("2");
        form.setSort("NEW");
        
        expected = new ProductSearchRequestDto();
        expected.setPriceMin(1000);
        expected.setPriceMax(2000);
        expected.setRating(3.5);
        expected.setPage(2);
        expected.setCategory("testCategory");
        expected.setKeyword("testKeyword");
        expected.setSort(SortKey.NEW);
    }

    @Test
    void toDto() {
        // act
        ProductSearchRequestDto actual = ProductConverter.toDto(form);
        
        // arrange
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
    
    @Test
    void toDto_withNull() {
        // arrange
        form.setPriceMin(null);
        form.setPriceMax(null);
        form.setRating(null);
        
        expected.setPriceMin(null);
        expected.setPriceMax(null);
        expected.setRating(null);
        
        // act
        ProductSearchRequestDto actual = ProductConverter.toDto(form);
        
        // arrange
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
