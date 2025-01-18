package com.example.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDocumentDto {

    @JsonProperty("product_id")
    private String productId;

    private String productName;

    private int price;

    private double averageRating;

    private int reviewCount;
    
    private String imagePath;

}
