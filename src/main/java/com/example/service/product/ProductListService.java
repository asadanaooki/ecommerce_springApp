package com.example.service.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch._types.SortOptions;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.RangeQuery;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.domain.model.dto.ProductDocumentDto;
import com.example.domain.model.dto.ProductSearchRequestDto;
import com.example.domain.model.dto.ProductSearchResponseDto;
import com.example.domain.model.enums.SortKey;

import lombok.RequiredArgsConstructor;

/**
 * 商品一覧を扱うサービスクラス
 */
@Service
@RequiredArgsConstructor
public class ProductListService {

    /**
     * Amazon OpenSearch Service 用のクライアント
     */
    private final OpenSearchClient osClient;

    /**
     * ページ番号リストで 1度に表示する最大数 (前後4ページずつ=9) 
     */
    private static final int PAGE_RANGE = 9;

    /**
     * 「現在ページを中心に前後4ページ」などで利用するオフセット
     */
    private static final int PAGE_OFFSET = 4;

    /**
     * 1ページあたりに表示する件数
     */
    @Value("${registration.display-number}")
    private int displayNumber;

    /**
     * <p>指定された検索条件 DTO をもとに、OpenSearch から商品リストを取得します。</p>
     *
     * @param dto 商品検索の条件を持つ DTO
     * @return 検索結果を格納した {@link ProductSearchResponseDto}
     * @throws OpenSearchException OpenSearch でエラーが発生した場合にスロー
     * @throws IOException 通信やシリアライズ時に IO エラーが発生した場合にスロー
     */
    public ProductSearchResponseDto searchProducts(ProductSearchRequestDto dto)
            throws OpenSearchException, IOException {
        // 検索クエリ構築
        BoolQuery boolQuery = buildBoolQuery(dto);
        List<SortOptions> sortOptions = buildSortOptions(dto.getSort());
        SearchRequest request = buildSearchRequest(boolQuery, sortOptions, dto.getPage());

        // 検索実行
        SearchResponse<ProductDocumentDto> response = osClient.search(request, ProductDocumentDto.class);

        return extractSearchResult(response, dto.getPage());
    }

    /**
     * Opensearchの検索結果を解析し、指定ページに対応する商品リストやページ情報を格納した
     * {@link ProductSearchResponseDto} を生成して返却します。
     *
     * @param response Elasticsearchの検索結果を保持する {@link SearchResponse} オブジェクト
     * @param page     現在のページ番号
     * @return 商品リスト・ページ情報・ページナビゲーション情報などを含む {@link ProductSearchResponseDto}
     */
    ProductSearchResponseDto extractSearchResult(SearchResponse<ProductDocumentDto> response,
            int page) {

        List<ProductDocumentDto> products = new ArrayList<>();
        // 総ヒット数
        double totalHits = 0d;

        if (response.hits().total() != null) {
            totalHits = (double) response.hits().total().value();
            for (Hit<ProductDocumentDto> hit : response.hits().hits()) {

                ProductDocumentDto data = hit.source();
                // TODO: 仮でstatic/imagesフォルダから取得
                data.setImagePath("/images/" + data.getProductId() + ".jpeg");
                products.add(data);
            }
        }
        // 総ヒット数から総ページ数を求め、
        // 小数点がある場合は切り上げてページ数を確定
        int totalPage = (int) Math.ceil(totalHits / displayNumber);

        // 現在のページが総ページより大きい場合は丸める
        if (page > totalPage) {
            page = totalPage;
        }

        // 前へ・次へボタンの表示制御
        boolean hasPrev = page > 1;
        boolean hasNext = page < totalPage;

        return new ProductSearchResponseDto(products, page, getPageNumbers(page, totalPage),
                hasPrev, hasNext);
    }

    /**
     * 指定されたクエリ（{@link BoolQuery}）、ソートオプション、ページ番号をもとに
     * Opensearchに送信する {@link SearchRequest} を構築して返却します。
     *
     * @param boolQuery   検索条件を表す {@link BoolQuery}
     * @param sortOptions ソート順を指定する {@link SortOptions} のリスト
     * @param page        取得したいページ番号
     * @return Elasticsearchへ送信するための {@link SearchRequest}
     */
    private SearchRequest buildSearchRequest(BoolQuery boolQuery, List<SortOptions> sortOptions, int page) {
        // データ取得開始位置
        int from = (page - 1) * displayNumber;

        return new SearchRequest.Builder()
                .index("product")
                .from(from)
                .size(displayNumber)
                .sort(sortOptions)
                // 商品コード、商品名、価格、平均評価、レビュー数を取得
                .source(sc -> sc.filter(f -> f
                        .includes("product_id", "product_name", "price", "average_rating", "review_count")))
                .query(q -> q.bool(boolQuery))
                .build();
    }

    /**
     * 現在のページ数・総ページ数をもとに表示すべきページ番号の一覧を生成し返却します。
     *
     * <p>総ページ数が {@value #PAGE_RANGE} 以下の場合は、1 から totalPage まですべてのページ番号を返却します。</p>
     *
     * <p>総ページ数が {@value #PAGE_RANGE} を超える場合は、現在ページを中心として前後
     * {@value #PAGE_OFFSET} ページを表示できるようにページ番号を制限します。</p>
     *
     * @param currentPage 現在のページ番号
     * @param totalPage   総ページ数
     * @return ページ番号のリスト
     */
    List<Integer> getPageNumbers(int currentPage, int totalPage) {
        // 総ページ数が 9 以下なら、1～totalPage をそのまま表示
        if (totalPage <= PAGE_RANGE) {
            return createSequence(1, totalPage);
        }

        // 通常は 前後4ページずつ → 合計で最大 9 ページ
        int startPage = currentPage - PAGE_OFFSET;
        int endPage = currentPage + PAGE_OFFSET;

        // 下限チェック: startPage < 1 なら、1～9 の範囲で表示
        if (startPage < 1) {
            startPage = 1;
            endPage = PAGE_RANGE;
        }

        // 上限チェック: endPage > totalPage なら、末尾側へ寄せて表示
        if (endPage > totalPage) {
            endPage = totalPage;
            startPage = endPage - (PAGE_RANGE - 1);
        }

        return createSequence(startPage, endPage);
    }

    /**
     * 指定された検索リクエストDTO ({@link ProductSearchRequestDto}) から
     * Opensearchで使用する {@link BoolQuery} を組み立てて返却します。
     *
     * <p>主に以下の条件に基づいて検索クエリを構築します:</p>
     * <ul>
     *     <li>販売期間（開始日が現在日以前、終了日が現在日以降）</li>
     *     <li>キーワード検索（商品名または商品説明に含まれる）</li>
     *     <li>カテゴリフィルタ</li>
     *     <li>価格帯フィルタ</li>
     *     <li>評価フィルタ</li>
     * </ul>
     *
     * @param dto 商品検索条件を保持する {@link ProductSearchRequestDto}
     * @return 組み立てられた {@link BoolQuery} オブジェクト
     */
    BoolQuery buildBoolQuery(ProductSearchRequestDto dto) {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        // 販売期間(販売開始日が今日以前 かつ 販売終了日が今日以降)
        boolBuilder
                // 開始日は「現在日」以前
                .must(m -> m
                        .range(r -> r
                                .field("start_sale_date")
                                .lte(JsonData.of("now/d"))))
                // 終了日は「現在日」以降
                .must(m -> m
                        .range(r -> r
                                .field("end_sale_date")
                                .gte(JsonData.of("now/d"))));

        //キーワード
        if (dto.getKeyword() != null) {
            // 複数のキーワードを空白区切りで分割する
            String[] words = dto.getKeyword().split("\\p{Z}+");
            BoolQuery.Builder orBuilder = new BoolQuery.Builder();
            for (String w : words) {
                orBuilder.should(s -> s
                        // 商品名または商品説明に含まれているか
                        .multiMatch(m -> m
                                .fields("product_name", "product_description")
                                .query(w)));
            }
            orBuilder.minimumShouldMatch("1");
            boolBuilder.must(m -> m.bool(orBuilder.build()));
        }

        // カテゴリフィルタ
        if (dto.getCategory() != null) {
            boolBuilder.filter(f -> f
                    .term(t -> t
                            .field("category_name")
                            .value(FieldValue.of(dto.getCategory()))));
        }

        // 価格帯フィルタ
        if (dto.getPriceMin() != null || dto.getPriceMax() != null) {
            RangeQuery.Builder priceRangeBuilder = new RangeQuery.Builder();

            if (dto.getPriceMin() != null) {
                // 最小価格以上
                priceRangeBuilder.field("price").gte(JsonData.of(dto.getPriceMin()));
            }

            if (dto.getPriceMax() != null) {
                // 最大価格以下
                priceRangeBuilder.field("price").lte(JsonData.of(dto.getPriceMax()));
            }
            boolBuilder.filter(f -> f.range(priceRangeBuilder.build()));
        }

        // 評価フィルタ
        if (dto.getRating() != null) {
            // 指定評価値以上
            boolBuilder.filter(f -> f
                    .range(r -> r
                            .field("average_rating")
                            .gte(JsonData.of(dto.getRating()))));
        }
        return boolBuilder.build();

    }

    /**
     * ソートキーに応じて、Opensearchのソートオプション ({@link SortOptions}) のリストを生成して返却します。
     *
     * <p>サポートしているソートキー:</p>
     * <ul>
     *     <li>PRICE_DESC: 価格が高い順</li>
     *     <li>PRICE_ASC: 価格が低い順</li>
     *     <li>RATING: 評価が高い順</li>
     *     <li>NEW: 新着順</li>
     * </ul>
     *
     * @param sortKey ソートキーを示す {@link SortKey} 列挙型
     * @return ソート順を指定する {@link SortOptions} のリスト
     */
    List<SortOptions> buildSortOptions(SortKey sortKey) {
        List<SortOptions> sortList = new ArrayList<>();
        
        if (sortKey == null) {
            return sortList;
        }

        switch (sortKey) {
        // 価格が高い順
        case PRICE_DESC:
            sortList.add(new SortOptions.Builder()
                    .field(f -> f.field("price").order(SortOrder.Desc))
                    .build());
            break;

        // 価格が低い順
        case PRICE_ASC:
            sortList.add(new SortOptions.Builder()
                    .field(f -> f.field("price").order(SortOrder.Asc))
                    .build());
            break;

        // 評価順
        case RATING:
            sortList.add(new SortOptions.Builder()
                    .field(f -> f.field("average_rating").order(SortOrder.Desc))
                    .build());
            break;

        // 新着順
        case NEW:
        default:
            sortList.add(new SortOptions.Builder()
                    .field(f -> f.field("start_sale_date").order(SortOrder.Desc))
                    .build());
            break;
        }

        return sortList;
    }

    /**
     * 指定された開始値と終了値の範囲を、連続する整数のリストとして生成して返却します。
     *
     * @param start 開始値
     * @param end   終了値
     * @return start～end の範囲に含まれるすべての整数を格納したリスト
     */
    private List<Integer> createSequence(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}
