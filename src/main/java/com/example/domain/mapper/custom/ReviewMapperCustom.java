package com.example.domain.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.mapper.ReviewMapper;
import com.example.domain.model.dto.ReviewDto;
import com.example.domain.model.dto.ReviewStatusDto;

@Mapper
public interface ReviewMapperCustom extends ReviewMapper {
    
    ReviewStatusDto selectReviewStatus(String productId);

    List<ReviewDto> selectRecentReviews(String productId);
}
