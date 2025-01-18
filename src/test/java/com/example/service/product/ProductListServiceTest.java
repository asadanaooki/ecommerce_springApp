package com.example.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch._types.ShardStatistics;
import org.opensearch.client.opensearch._types.SortOptions;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.domain.model.dto.ProductDocumentDto;
import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.dto.ProductSearchResponseDto;
import com.example.domain.model.enums.SortKey;

@ExtendWith(MockitoExtension.class)
class ProductListServiceTest {

    @Spy
    @InjectMocks
    ProductListService productListService;

    @Mock
    OpenSearchClient openSearchClient;
    
    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(productListService, "displayNumber", 10);
    }

    @Test
    void searchProducts() throws OpenSearchException, IOException {
        // arrange
        ProductSearchRequestDto dto = new ProductSearchRequestDto();
        dto.setPage(1);
        dto.setSort(SortKey.NEW);

        SearchResponse<ProductDocumentDto> mockResponse = buildSearchResponse(3);

        when(openSearchClient.search(any(SearchRequest.class), eq(ProductDocumentDto.class)))
                .thenReturn(mockResponse);

        // act
        ProductSearchResponseDto resDto = productListService.searchProducts(dto);

        // assert
        assertNotNull(resDto);
        
        ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);
        
        // TODO:
//        verify(openSearchClient).search(captor.capture(), eq(ProductDocumentDto.class));
//        SearchRequest capRequest = captor.getValue();
//        
//        assertThat(capRequest.index()).isEqualTo("product");
//        assertThat(capRequest.from()).isEqualTo(0);
//        assertThat(capRequest.size()).isEqualTo(10);
//        assertThat(capRequest.sort()).isEqualTo(List.of(
//                new SortOptions.Builder()
//                .field(f -> f.field("start_sale_date").order(SortOrder.Desc))
//                .build()));
//// sourceのテストコードも書くべきか？
        
        
        // 各メソッドが1回呼ばれてることを確認
        verify(productListService).buildBoolQuery(dto);
        verify(productListService).buildSortOptions(dto.getSort());
        verify(openSearchClient).search(any(SearchRequest.class), eq(ProductDocumentDto.class));
        verify(productListService).extractSearchResult(mockResponse, dto.getPage());

    }

    @Nested
    class extractSearchResult {
        @Test
        void extractSearchResult_singleProduct() {
            // arrange
            SearchResponse<ProductDocumentDto> response = buildSearchResponse(1);
            // 期待値のオブジェクトを準備
            ProductDocumentDto expected = new ProductDocumentDto();
            expected.setProductId("product-1");
            expected.setProductName("Test Product 1");
            expected.setPrice(1000);
            expected.setAverageRating(0.5);
            expected.setReviewCount(2);
            expected.setImagePath("/images/product-1.jpeg");

            // act
            ProductSearchResponseDto acutual = productListService.extractSearchResult(response, 1);

            // assert
            assertThat(acutual.getProducts()).hasSize(1);
            assertThat(acutual.getProducts().get(0)).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void extractSearchResult_NoHits() {
            // arrange
            SearchResponse<ProductDocumentDto> response = buildSearchResponse(0);

            // act
            ProductSearchResponseDto result = productListService.extractSearchResult(response, 1);

            // assert
            assertThat(result.getProducts()).isEmpty();
            assertThat(result.getCurrentPage()).isEqualTo(0);
            assertThat(result.getPageNumbers()).isEmpty();
            assertThat(result.isHasPrev()).isFalse();
            assertThat(result.isHasNext()).isFalse();
        }

        @Test
        void extractSearchResult_hits() {
            // arrange
            SearchResponse<ProductDocumentDto> response = buildSearchResponse(21);

            // act
            ProductSearchResponseDto result = productListService.extractSearchResult(response, 2);

            // assert
            assertThat(result.getProducts()).hasSize(21);
            assertThat(result.getCurrentPage()).isEqualTo(2);
            assertThat(result.getPageNumbers()).isEqualTo(List.of(1, 2, 3));
            assertThat(result.isHasPrev()).isTrue();
            assertThat(result.isHasNext()).isTrue();

        }
    }

    @Nested
    class buildBoolQuery {
        ProductSearchRequestDto dto;

        @BeforeEach
        void setup() {
            dto = new ProductSearchRequestDto();
            // ソートはデフォルトで新着順
            dto.setSort(SortKey.NEW);
        }

        @Test
        void buildBoolQuery_allParams() {
            // arrange
            dto.setKeyword("apple orange");
            dto.setCategory("Fruits");
            dto.setPriceMin(100);
            dto.setPriceMax(300);
            dto.setRating(4.5);

            // act
            BoolQuery boolQuery = productListService.buildBoolQuery(dto);

            // assert
            List<Query> mustList = boolQuery.must();

            assertThat(mustList).hasSize(3);
            assertThat(mustList.subList(0, 2));

            List<String> queries = mustList.get(2)
                    .bool()
                    .should().stream()
                    .map(m -> m.multiMatch().query())
                    .toList();

            assertThat(queries).containsExactly("apple", "orange");

            List<Query> filterList = boolQuery.filter();

            assertThat(filterList).hasSize(3);

            assertThat(filterList.get(0).term()).satisfies(s -> {
                assertThat(s.field()).isEqualTo("category_name");
                assertThat(s.value().stringValue()).isEqualTo("Fruits");
            });

            assertNumericRange(filterList.get(1), "price", "100", "300");
            assertNumericRange(filterList.get(2), "average_rating", "4.5", null);
        }

        @Test
        void buildBoolQuery_noParams() {
            // act
            BoolQuery boolQuery = productListService.buildBoolQuery(dto);

            // assert
            List<Query> mustList = boolQuery.must();

            assertStartAndEndSaleDateRange(mustList);

            assertThat(boolQuery.filter()).isEmpty();
        }

        @Test
        void buildBoolQuery_priceMinOnly() {
            // arrange
            dto.setPriceMin(100);

            // act
            BoolQuery boolQuery = productListService.buildBoolQuery(dto);

            // assert
            assertThat(boolQuery.must()).hasSize(2);
            assertThat(boolQuery.filter()).hasSize(1);

            assertThat(boolQuery.filter().get(0).range()).satisfies(s -> {
                assertThat(s.gte().to(Integer.class)).isEqualTo(100);
                assertThat(s.lte()).isEqualTo(null);

            });

        }

        @Test
        void buildBoolQuery_priceMaxOnly() {
            // arrange
            dto.setPriceMax(500);

            // act
            BoolQuery boolQuery = productListService.buildBoolQuery(dto);

            // assert
            assertThat(boolQuery.must()).hasSize(2);
            assertThat(boolQuery.filter()).hasSize(1);

            assertThat(boolQuery.filter().get(0).range()).satisfies(s -> {
                assertThat(s.lte().to(Integer.class)).isEqualTo(500);
                assertThat(s.gte()).isEqualTo(null);

            });

        }

        void assertStartAndEndSaleDateRange(List<Query> mustList) {
            assertThat(mustList)
                    .extracting(Query::range)
                    .extracting(r -> r.field(),
                            r -> r.lte() == null ? null : r.lte().toString(),
                            r -> r.gte() == null ? null : r.gte().toString())
                    .containsExactly(tuple("start_sale_date", "now/d", null),
                            tuple("end_sale_date", null, "now/d"));
        }

        void assertNumericRange(Query query, String fieldName,
                String expectedGte, String expectedLte) {
            assertThat(query.range()).satisfies(s -> {
                assertThat(s.gte() == null ? null : s.gte().toString()).isEqualTo(expectedGte);
                assertThat(s.lte() == null ? null : s.lte().toString()).isEqualTo(expectedLte);
            });
        }

    }

    @ParameterizedTest
    @MethodSource("provideSortkeyData")
    void checkBuildSortOptions(SortKey sortKey, String expectedField, SortOrder expectedOrder) {
        List<SortOptions> sortOptions = productListService.buildSortOptions(sortKey);

        if (sortKey == null) {
            assertThat(sortOptions).isEmpty();
        } else {
            assertThat(sortOptions)
                    .extracting(s -> s.field().field(),
                            s -> s.field().order())
                    .containsExactly(tuple(expectedField, expectedOrder));
        }
    }

    static Stream<Arguments> provideSortkeyData() {
        return Stream.of(
                Arguments.of(SortKey.NEW, "start_sale_date", SortOrder.Desc),
                Arguments.of(SortKey.PRICE_DESC, "price", SortOrder.Desc),
                Arguments.of(SortKey.PRICE_ASC, "price", SortOrder.Asc),
                Arguments.of(SortKey.RATING, "average_rating", SortOrder.Desc),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("providePageNumberArguments")
    void getPageNumbers_parameterized(int currentPage, int totalPage, List<Integer> expectedList) {
        // act
        List<Integer> actual = productListService.getPageNumbers(currentPage, totalPage);

        // assert
        assertThat(actual).containsExactlyElementsOf(expectedList);
    }

    static Stream<Arguments> providePageNumberArguments() {
        return Stream.of(
                // 総ページ数チェック:
                Arguments.of(
                        2,
                        8,
                        List.of(1, 2, 3, 4, 5, 6, 7, 8)),

                // 下限チェック:
                Arguments.of(
                        1,
                        10,
                        List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),

                // 上限チェック:
                Arguments.of(
                        10,
                        10,
                        List.of(2, 3, 4, 5, 6, 7, 8, 9, 10)));
    }

    SearchResponse<ProductDocumentDto> buildSearchResponse(int count) {
        // 1. ProductDocumentDto と Hit を作成
        List<Hit<ProductDocumentDto>> hitList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ProductDocumentDto doc = new ProductDocumentDto();
            doc.setProductId("product-" + i);
            doc.setProductName("Test Product " + i);
            doc.setPrice(i * 1000);
            doc.setAverageRating(i * 0.5);
            doc.setReviewCount(i * 2);

            Hit<ProductDocumentDto> hit = new Hit.Builder<ProductDocumentDto>()
                    .source(doc)
                    .build();
            hitList.add(hit);
        }

        // 2. HitsMetadata にリストを詰める
        HitsMetadata<ProductDocumentDto> hitsMetadata = new HitsMetadata.Builder<ProductDocumentDto>()
                .hits(hitList)
                .total(t -> t.value(count).relation(TotalHitsRelation.Eq))
                .build();

        // 3. ShardStatistics を適当に用意（テスト用）
        ShardStatistics shardStats = new ShardStatistics.Builder()
                .total(1) // シャード数
                .successful(1) // 成功シャード数
                .skipped(0)
                .failed(0)
                .build();

        // 4. SearchResponse に上記情報をセット
        return new SearchResponse.Builder<ProductDocumentDto>()
                .took(123L) // took は任意の値
                .timedOut(false) // 適宜指定
                .shards(shardStats)
                .hits(hitsMetadata)
                .build();
    }

}
