package com.example.validation.validator;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.validation.annotation.StringIntegerRange;

/**
 * {@link StringIntegerRange} アノテーションが付与された文字列表現の数値が、
 * 指定された範囲（{@link StringIntegerRange#min()} ～ {@link StringIntegerRange#max()}）内の整数かどうかを検証するためのバリデータクラスです。
 * <p>
 * バリデーションでは、以下のステップでチェックを行います:
 * </p>
 * <ol>
 *   <li>値が <code>null</code> かどうかを確認し、<code>null</code> なら不正とみなす。</li>
 *   <li>{@link BigDecimal} に変換できるかどうかを確認する。</li>
 *   <li>{@link BigDecimal#stripTrailingZeros()} を用いて末尾の不要なゼロを除去する。</li>
 *   <li>{@link BigDecimal#scale()} が 0 より大きい場合は、小数点以下の値があるため不正とみなす。</li>
 *   <li>{@link BigDecimal#intValueExact()} で整数値を取得し、指定の <code>min</code> 以上、<code>max</code> 以下かをチェックする。</li>
 *   <li>これらの条件をすべて満たした場合にのみ <code>true</code> を返却する。</li>
 * </ol>
 * <p>
 * 変換に失敗した場合や整数値への変換に問題があった場合は <code>false</code> を返し、不正として扱います。
 * </p>
 *
 * @see StringIntegerRange
 */
public class StringIntegerRangeValidator implements ConstraintValidator<StringIntegerRange, String> {

    /** 検証対象となる最小値 */
    private int min;

    /** 検証対象となる最大値 */
    private int max;

    /**
     * アノテーションに設定されたパラメータ（min, max など）を受け取り、
     * 本バリデータのフィールドにセットします。
     *
     * @param constraintAnnotation {@link StringIntegerRange} アノテーションのインスタンス
     */
    @Override
    public void initialize(StringIntegerRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    /**
     * 実際のバリデーションロジックを行います。
     * <p>
     * <ul>
     *   <li>入力が <code>null</code> の場合は <code>false</code> を返却</li>
     *   <li>整数として正しくパースできるか</li>
     *   <li>小数点以下を含む値でないか</li>
     *   <li><code>min</code> 以上、<code>max</code> 以下であるか</li>
     * </ul>
     * </p>
     *
     * @param value   検証対象の文字列
     * @param context バリデーションコンテキスト
     * @return 文字列が指定範囲内の整数として有効であれば <code>true</code>、それ以外は <code>false</code>
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        try {
            // 文字列を BigDecimal に変換
            BigDecimal bd = new BigDecimal(value);

            // 末尾の不要なゼロを削除
            bd = bd.stripTrailingZeros();

            // 小数点以下が存在する場合は不正と判断
            if (bd.scale() > 0) {
                return false;
            }

            // 整数値を取得（範囲外の場合は例外が発生する可能性あり）
            int num = bd.intValueExact();

            // min <= num <= max を確認
            return (min <= num && num <= max);
        } catch (NumberFormatException | ArithmeticException e) {
            // 変換エラー、もしくはintValueExactが失敗した場合は不正とする
            return false;
        }
    }
}
