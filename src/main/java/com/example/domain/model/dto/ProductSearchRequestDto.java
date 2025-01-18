package com.example.domain.model.dto;

import com.example.domain.model.enums.SortKey;

import lombok.Data;

/**
 * 
 */
@Data
public class ProductSearchRequestDto {

    /**
     * 現在のページ番号
     */
    private int page;

    /**
     * 検索キーワード
     */
    private String keyword;

    /**
     * カテゴリ
     */
    private String category;

    /**
     * 最小価格
     */
    private Integer priceMin;

    /**
     * 最大価格
     */
    private Integer priceMax;

    /**
     * 評価値
     */
    private Double rating;

    /**
     * ソート順
     */
    private SortKey sort;
}
