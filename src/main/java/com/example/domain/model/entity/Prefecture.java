package com.example.domain.model.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 都道府県エンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prefecture {

	/**
	 * 都道府県ID
	 */
	private String prefectureId;
	
	/**
	 * 都道府県名
	 */
	private String prefectureName;
	
	/**
	 * 表示順序
	 */
	private int displayOrder;
	
	/**
	 * 作成日時
	 */
	private LocalDateTime createdAt;

	/**
	 * 更新日時
	 */
	private LocalDateTime updatedAt;
}