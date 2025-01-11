package com.example.web.controller.product;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Valid;

import org.opensearch.client.opensearch._types.OpenSearchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.converter.product.ProductConverter;
import com.example.domain.model.dto.ProductSearchResponseDto;
import com.example.domain.model.enums.SortKey;
import com.example.service.product.ProductListService;
import com.example.web.form.ProductSearchForm;

import lombok.AllArgsConstructor;

/**
 * 商品の一覧検索画面を制御するコントローラクラスです。
 * <p>
 * ユーザーから入力された検索フォーム（{@link ProductSearchForm}）を受け取り、
 * バリデーション結果をもとにパラメータを調整したうえで商品検索を実行します。
 * 検索結果は {@link ProductSearchResponseDto} として取得し、画面に表示します。
 * </p>
 */
@Controller
@AllArgsConstructor
@RequestMapping("/product")
public class ProductListController {

    /** 商品一覧に関するサービスクラス */
    private final ProductListService productListService;

    /** 検索キーワードの最大文字数 */
    private static final int KEYWORD_MAX_LENGTH = 100;

    /**
     * 商品検索を実行し、結果を画面に渡します。
     * <p>
     * フォームにバリデーションエラーが含まれる場合、{@link #adjustParameter(ProductSearchForm, BindingResult)} によって
     * フォームのパラメータを再調整してから再度検索を行います。
     * </p>
     *
     * @param form   入力フォーム（{@link ProductSearchForm}）
     * @param result バリデーション結果
     * @param model  画面へオブジェクトを渡すためのモデル
     * @return 商品一覧を表示するビュー名（<code>product/productList</code>）
     * @throws OpenSearchException OpenSearch 関連のエラーが発生した場合にスローされる例外
     * @throws IOException         IO 関連エラーが発生した場合にスローされる例外
     */
    @GetMapping
    public String SearchProduct(@Valid @ModelAttribute("form") ProductSearchForm form,
            BindingResult result,
            Model model) throws OpenSearchException, IOException {

        // バリデーションエラーがある場合、パラメータを調整
        adjustParameter(form, result);

        // DTO に変換したフォーム情報をもとに商品検索を行う
        ProductSearchResponseDto searchResult = productListService.searchProducts(ProductConverter.toDto(form));
        model.addAttribute("result", searchResult);

        return "product/productList";
    }

    /**
     * バリデーションでエラーになったフィールドを再調整し、検索に利用できる形に補正します。
     * <p>
     * 具体的には、下記のような処理を行います:
     * </p>
     * <ul>
     *   <li>page: エラーの場合、<code>"1"</code> にリセット</li>
     *   <li>keyword: エラーの場合、最大長（{@value #KEYWORD_MAX_LENGTH}）に切り詰める</li>
     *   <li>category: エラーの場合、<code>null</code> に置換</li>
     *   <li>priceMin/priceMax: エラーの場合、<code>null</code> に置換</li>
     *   <li>sort: エラーの場合、{@link SortKey#NEW} に設定</li>
     * </ul>
     *
     * @param form   商品検索フォーム
     * @param result バリデーション結果
     */
    void adjustParameter(ProductSearchForm form, BindingResult result) {
        Set<String> processedFields = new HashSet<String>();

        for (FieldError fieldError : result.getFieldErrors()) {
            String fieldName = fieldError.getField();

            // 既に処理済みのフィールドはスキップ
            if (processedFields.contains(fieldName)) {
                continue;
            }

            switch (fieldName) {
            case "page":
                form.setPage("1");
                break;

            case "keyword":
                // 空白時はnull,それ以外はトリミング
                if (form.getKeyword() == null || form.getKeyword().isBlank()) {
                    form.setKeyword(null);
                } else {
                    form.setKeyword(form.getKeyword().substring(0, KEYWORD_MAX_LENGTH));
                }
                break;

            case "category":
                form.setCategory(null);
                break;

            case "priceMin":
                form.setPriceMin(null);
                break;

            case "priceMax":
                form.setPriceMax(null);
                break;

            case "sort":
                // 新着順に設定
                form.setSort(SortKey.NEW.toString());
                break;
            }
            processedFields.add(fieldName);
        }
    }
}
