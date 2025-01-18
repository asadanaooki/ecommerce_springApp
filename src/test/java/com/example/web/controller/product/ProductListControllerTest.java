package com.example.web.controller.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.converter.bean.BeanMapConverter;
import com.example.domain.model.dto.ProductDocumentDto;
import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.dto.ProductSearchResponseDto;
import com.example.domain.model.enums.SortKey;
import com.example.service.product.ProductListService;
import com.example.web.form.ProductSearchForm;

@WebMvcTest(ProductListController.class)
@WithMockUser
class ProductListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductListService productListService;

    @SpyBean
    ProductListController productListController;

    @Test
    void searchProduct() throws Exception {
        // arrange
        ProductSearchResponseDto mockResponse = new ProductSearchResponseDto(
                List.of(new ProductDocumentDto(),
                        new ProductDocumentDto(),
                        new ProductDocumentDto()),
                2,
                List.of(1, 2, 3),
                true,
                true);
        when(productListService.searchProducts(any(ProductSearchRequestDto.class)))
                .thenReturn(mockResponse);

        ProductSearchForm form = new ProductSearchForm("", "word", "category", "100", "300", "4.0", "NEW");

        // act
        MvcResult mvcResult = mockMvc.perform(get("/product")
                .params(BeanMapConverter.beanToMultiValueMap(form)))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productList"))
                .andExpect(model().attributeExists("result"))
                .andReturn();

        // assert
        verify(productListController)
                .adjustParameter(any(ProductSearchForm.class), any(BindingResult.class));
        ProductSearchResponseDto actual = (ProductSearchResponseDto) mvcResult.getModelAndView().getModel()
                .get("result");

        assertThat(actual).usingRecursiveComparison().isEqualTo(mockResponse);
    }

    @Nested
    class adjustParameter {
        
        ProductSearchForm form;

        BindingResult result;

        ProductSearchForm expected;

        @BeforeEach
        void setup() {

            this.form = new ProductSearchForm();
            // 初期値をセット（実際の値は適宜変更）
            form.setPage("1");
            form.setKeyword("someKeyword");
            form.setCategory("someCategory");
            form.setPriceMin("100");
            form.setPriceMax("999");
            form.setRating("3.5");
            form.setSort(SortKey.NEW.toString());

            this.expected = new ProductSearchForm();
            expected.setPage("1");
            expected.setKeyword("someKeyword");
            expected.setCategory("someCategory");
            expected.setPriceMin("100");
            expected.setPriceMax("999");
            expected.setRating("3.5");
            expected.setSort(SortKey.NEW.toString());

            this.result = new BeanPropertyBindingResult(form, "form");
        }

        @Test
        void adjustParameter_noError() {
            // act
            productListController.adjustParameter(form, result);

            // assert
            assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        }

        @Test
        void adjustParameter_allParamError() {
            // arrange
            form.setPage("ab123456789");
            form.setKeyword("a".repeat(101));
            form.setCategory("a".repeat(51));
            form.setPriceMin(null);
            form.setPriceMax(null);
            form.setRating(null);
            form.setSort("aa");

            expected.setPage("1");
            expected.setKeyword("a".repeat(101).substring(0, 100));
            expected.setCategory(null);
            expected.setPriceMin(null);
            expected.setPriceMax(null);
            expected.setRating(null);
            expected.setSort(SortKey.NEW.toString());

            // pageは複数エラーを想定
            result.addError(new FieldError("form", "page", null));
            result.addError(new FieldError("form", "page", null));
            
            result.addError(new FieldError("form", "keyword", null));
            result.addError(new FieldError("form", "category", null));
            result.addError(new FieldError("form", "priceMin", null));
            result.addError(new FieldError("form", "priceMax", null));
            result.addError(new FieldError("form", "sort", null));

            // act
            productListController.adjustParameter(form, result);

            // assert
            assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        }
    }

}
