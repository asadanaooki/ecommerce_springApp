package com.example.service.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.constant.PrefixConstant;
import com.example.domain.mapper.ProductMapper;
import com.example.domain.mapper.custom.ReviewMapperCustom;
import com.example.domain.model.dto.ProductDetailDto;
import com.example.domain.model.dto.ReviewDto;
import com.example.domain.model.dto.ReviewStatusDto;
import com.example.domain.model.entity.Product;
import com.example.domain.model.entity.ProductExample;
import com.example.util.Redis.RedisUtil;
import com.example.util.Security.SecurityUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductDetailService {

    private final ProductMapper productMapper;

    private final ReviewMapperCustom reviewMapperCustom;

    private final SecurityUtil securityUtil;

    private final RedisUtil redisUtil;

    public ProductDetailDto getProductDetail(String productId) {
        // 商品情報を取得
        Product product = fetchProduct(productId);

        // お気に入りフラグ
        boolean isFavorite = false;
        // ログイン中のみ、お気に入り登録されているか確認する
        if (securityUtil.isLogin()) {
            String key = PrefixConstant.FAVORITE + securityUtil.getLoginUserId();
            isFavorite = redisUtil.isMemberOfSet(key, productId);
        }

        // レビュー関連
        ReviewStatusDto dto = reviewMapperCustom.selectReviewStatus(productId);
        List<ReviewDto> list = reviewMapperCustom.selectRecentReviews(productId);

        return toDto(product, isFavorite, dto, list);
    }

    public void addFavorite(String productId) {
        // お気に入り追加時は必ずログインしている
        String key = PrefixConstant.FAVORITE + securityUtil.getLoginUserId().get();
        redisUtil.addToSet(key, productId);
    }

    public void removeFavorite(String productId) {
     // お気に入り解除時は必ずログインしている
        String key = PrefixConstant.FAVORITE + securityUtil.getLoginUserId().get();
        redisUtil.removeFromSet(key, productId);
    }

    Product fetchProduct(String productId) {
        ProductExample example = new ProductExample();

        example.createCriteria()
                .andProductIdEqualTo(productId)
                .andStartSaleDateLessThanOrEqualTo(LocalDate.now())
                .andEndSaleDateGreaterThanOrEqualTo(LocalDate.now());

        return productMapper.selectByExample(example).get(0);
    }

    private ProductDetailDto toDto(Product product, boolean isFavorite,
            ReviewStatusDto reviewStatus, List<ReviewDto> recentReviews) {
        ProductDetailDto dto = new ProductDetailDto();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());

        dto.setStock(product.getStock() > 0);
        dto.setImagePath("/images/" + product.getProductId() + ".jpeg");
        dto.setFavorite(isFavorite);

        // 平均評価の小数第二位以降切り捨て
        BigDecimal bd = reviewStatus== null ? BigDecimal.ZERO :
                (reviewStatus.getAverageRating().setScale(1, RoundingMode.FLOOR));
        dto.setAverageRating(bd);
        
        dto.setReviewCount(reviewStatus.getReviewCount());
        dto.setRecentReviews(recentReviews);

        return dto;
    }

}
