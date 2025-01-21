package com.example.service.product;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.constant.PrefixConstant;
import com.example.domain.mapper.ProductMapper;
import com.example.domain.model.entity.Product;
import com.example.util.Redis.RedisUtil;
import com.example.util.Security.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {
    
    @Mock
    SecurityUtil securityUtil;
    
    @Mock
    RedisUtil redisUtil;
    
    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductDetailService productDetailService;

    String productId = "product-123";
    String userId = "user-999";
    
    Product product;
    
    @BeforeEach
    void setup() {
        product = new Product();

        // 各フィールドにテスト用の適当な値を設定
        product.setProductId("product-123");
        product.setProductName("サンプル商品");
        product.setPrice(1200);
        product.setProductDescription("テスト用の説明文です。");
        product.setStock(30);
        product.setCategoryId("CAT99");
        product.setStartSaleDate(LocalDate.of(2023, 1, 1));
        product.setEndSaleDate(LocalDate.of(2026, 12, 31));
    }

    @Test
    void fetchProduct() {

    }

    @Test
    void addFavorite() {
        // arrange
        String expectedKey = PrefixConstant.FAVORITE + userId;
        when(securityUtil.getLoginUserId()).thenReturn(Optional.of(userId));
        
        // act
        productDetailService.addFavorite(productId);
        
        // assert
        verify(redisUtil).addToSet(expectedKey, productId);
    }
    
    @Test
    void removeFavorite() {
        // arrange
        String expectedKey = PrefixConstant.FAVORITE + userId;
        when(securityUtil.getLoginUserId()).thenReturn(Optional.of(userId));
        
        // act
        productDetailService.removeFavorite(productId);
        
        // assert
        verify(redisUtil).removeFromSet(expectedKey, productId);

    }

}
