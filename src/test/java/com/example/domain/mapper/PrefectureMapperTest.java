package com.example.domain.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.domain.model.entity.Prefecture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PrefectureMapperTest extends MapperTestBase {

	PrefectureMapperTest() {
		super(TEST_DATA_FILE_PATH);
	}

	static final String TEST_DATA_FILE_PATH = "data/prefectureMapper/init_data.xml";

	@Autowired
	PrefectureMapper prefectureMapper;

	@Test
	void getAllPrefectures() {
		List<Prefecture> list = prefectureMapper.getAllPrefectures();

		// 件数確認
		assertThat(list).hasSize(47);

		// 代表的なデータを確認
		assertThat(list.get(0).getPrefectureId())
				.isEqualTo("b2b5c1773047b8ec73e8fdd70b30e090c50ec83b78fc90ad1f248bb58617");
		assertThat(list.get(0).getPrefectureName()).isEqualTo("北海道");

		assertThat(list.get(24).getPrefectureId())
				.isEqualTo("6bf8171851afa331b34154e674f55d10c5fd997345d5816db6c2c0de80dc");
		assertThat(list.get(24).getPrefectureName()).isEqualTo("大阪府");

		assertThat(list.get(46).getPrefectureId())
				.isEqualTo("836c7c1cbff5ce7cb0509c7a03c922fc25caee48b433fa38cf04db89b64f");
		assertThat(list.get(46).getPrefectureName()).isEqualTo("沖縄県");

		// 順序確認
		List<Integer> orders = list.stream().map(Prefecture::getDisplayOrder).collect(Collectors.toList());

		assertThat(orders).isSorted();
	}

}
