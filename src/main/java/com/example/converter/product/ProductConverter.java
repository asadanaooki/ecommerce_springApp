package com.example.converter.product;

import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.enums.SortKey;
import com.example.web.form.ProductSearchForm;

/**
 * 商品用のコンバータークラス
 */
public class ProductConverter {

    /**
     * 商品検索用のDtoに変換する
     * @param form 商品検索フォーム
     * @return 商品検索Dto
     */
    public static ProductSearchRequestDto toDto(ProductSearchForm form) {
        ProductSearchRequestDto dto = new ProductSearchRequestDto();
        
        Integer priceMin = form.getPriceMin() == null ? null : Integer.parseInt(form.getPriceMin());
        Integer priceMax = form.getPriceMax() == null ? null : Integer.parseInt(form.getPriceMax());
        Double rating = form.getRating() == null ? null : Double.parseDouble(form.getRating());

        dto.setPage(Integer.parseInt(form.getPage()));
        dto.setCategory(form.getCategory());
        dto.setKeyword(form.getKeyword());
        dto.setPriceMin(priceMin);
        dto.setPriceMax(priceMax);
        dto.setRating(rating);
        dto.setSort(SortKey.valueOf(form.getSort()));

        return dto;
    }

}
