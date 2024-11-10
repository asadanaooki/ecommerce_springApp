package com.example.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.model.entity.Prefecture;

/**
 * 都道府県情報を操作するためのマッパー
 */
@Mapper
public interface PrefectureMapper {

	/**
	 * すべての都道府県情報を取得
	 * 
	 * @return 都道府県一覧
	 */
	List<Prefecture> getAllPrefectures();
}
