package com.example.service.user;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.domain.mapper.PrefectureMapper;
import com.example.domain.model.entity.Prefecture;

import lombok.AllArgsConstructor;

/**
 * 都道府県に関連するサービスクラス
 */
@Service
@AllArgsConstructor
public class PrefectureService {
	/**
	 * 都道府県データを取得するためのマッパー
	 */
	private final PrefectureMapper prefectureMapper;

	/**
	 * 全ての都道府県データを取得する
	 * 
	 * @return 全ての都道府県データ
	 */
	@Cacheable("prefecture")
	public List<Prefecture> getAllPrefectures() {
		return prefectureMapper.getAllPrefectures();
	}

}
