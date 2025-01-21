package com.example.domain.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ReviewStatusDto {
    
    private BigDecimal averageRating;
    
    private int reviewCount;

}
