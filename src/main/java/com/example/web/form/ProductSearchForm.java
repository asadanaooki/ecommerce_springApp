package com.example.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.enums.SortKey;
import com.example.validation.annotation.StringDecimalRange;
import com.example.validation.annotation.StringIntegerRange;
import com.example.validation.annotation.ValidSortKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchForm {

    /**
     * 現在のページ番号
     */
    @NotBlank
    @Length(max = 9)
    @Pattern(regexp = "^[1-9]\\d*(\\.0+)?$")
    private String page;

    /**
     * 検索キーワード
     */
    @NotBlank
    @Length(max = 100)
    private String keyword;

    /**
     * カテゴリ
     */
    @NotBlank
    @Length(max = 50)
    private String category;

    /**
     * 最小価格
     */
    @NotBlank
    @Length(max = 9)
    @StringIntegerRange(min = 0, max = 999999999)
    private String priceMin;

    /**
     * 最大価格
     */
    @NotBlank
    @Length(max = 9)
    @StringIntegerRange(min = 0, max = 999999999)
    private String priceMax;

    /**
     * 評価値
     */
    @NotBlank
    @Length(max = 13)
    @StringDecimalRange(min = 0.0, max = 5.0)
    private String rating;

    /**
     * ソート順
     */
    @ValidSortKey
    private String sort;
    
    /**
     * 商品検索用のDtoに変換する
     * @return 商品検索Dto
     */
    public ProductSearchRequestDto toDto() {
        ProductSearchRequestDto dto = new ProductSearchRequestDto();
        
        Integer priceMin = getPriceMin() == null ? null : Integer.parseInt(getPriceMin());
        Integer priceMax = getPriceMax() == null ? null : Integer.parseInt(getPriceMax());
        Double rating = getRating() == null ? null : Double.parseDouble(getRating());

        dto.setPage(Integer.parseInt(getPage()));
        dto.setCategory(getCategory());
        dto.setKeyword(getKeyword());
        dto.setPriceMin(priceMin);
        dto.setPriceMax(priceMax);
        dto.setRating(rating);
        dto.setSort(SortKey.valueOf(getSort()));

        return dto;
    }
}
