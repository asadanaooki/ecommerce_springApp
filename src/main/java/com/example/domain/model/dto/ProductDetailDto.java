package com.example.domain.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductDetailDto {

    private String productId;
    private String productName;
    private String productDescription;
    private int price;
    private boolean isStock; 
    private boolean isFavorite;
    private String imagePath;     // /images/xxx.png
    private BigDecimal averageRating; // 小数第一位
    private int reviewCount;
    private List<ReviewDto> recentReviews; // 新着3件
}
