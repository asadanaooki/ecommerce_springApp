package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.domain.mapper.PrefectureMapper;
import com.example.domain.model.entity.Prefecture;

@SpringBootTest
class PrefectureServiceTest {

	@Autowired
	PrefectureService prefectureService;

	@MockBean
	PrefectureMapper prefectureMapper;

	@Test
	void getAllPrefectures() {
		// モックの戻り値を設定
		List<Prefecture> mockList = List.of(new Prefecture("1", "北海道", 1, null, null),
				new Prefecture("2", "青森県", 2, null, null));
		when(prefectureMapper.getAllPrefectures()).thenReturn(mockList);

		List<Prefecture> list = prefectureService.getAllPrefectures();
		
		assertThat(list).isEqualTo(mockList);
	}

}
