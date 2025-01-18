package com.example.domain.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 */
@Data
@AllArgsConstructor
public class ProductSearchResponseDto {

    /**
     * 
     */
    private List<ProductDocumentDto> products;
    
    /**
     * 
     */
    private int currentPage;
    
    /**
     * 
     */
    private List<Integer> pageNumbers;
    
    /**
     * 
     */
    private boolean hasPrev;
    
    /**
     * 
     */
    private boolean hasNext;
}
