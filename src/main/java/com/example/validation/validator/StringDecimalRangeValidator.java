package com.example.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.validation.annotation.StringDecimalRange;

/**
 * {@link StringDecimalRange} アノテーションが付与された文字列を数値（<code>double</code>）に変換し、
 * {@link StringDecimalRange#min()} ～ {@link StringDecimalRange#max()} の範囲に収まっているかを検証するバリデータです。
 * <p>
 * 文字列が <code>null</code> だったり、数値に変換できない形式の場合は不正とみなします。
 * </p>
 *
 * @see StringDecimalRange
 */
public class StringDecimalRangeValidator implements ConstraintValidator<StringDecimalRange, String> {

    /** 許容する数値の最小値 */
    private double min;

    /** 許容する数値の最大値 */
    private double max;

    /**
     * アノテーションに設定されたパラメータ（<code>min</code>, <code>max</code>）を取得し、
     * バリデータのフィールドにセットします。
     *
     * @param constraintAnnotation {@link StringDecimalRange} アノテーションのインスタンス
     */
    @Override
    public void initialize(StringDecimalRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    /**
     * フィールドの値を <code>double</code> に変換し、設定された範囲内にあるかをチェックします。
     * <p>
     * <ul>
     *   <li><code>value</code> が <code>null</code> の場合は <code>false</code> を返却</li>
     *   <li>数値への変換に失敗した場合（<code>NumberFormatException</code>）も不正とみなす</li>
     *   <li><code>min</code> 以上、<code>max</code> 以下なら <code>true</code> を返却</li>
     * </ul>
     * </p>
     *
     * @param value   検証対象の文字列
     * @param context バリデーションコンテキスト
     * @return 指定範囲内の数値なら <code>true</code>、それ以外は <code>false</code>
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value == null) {
                return false;
            }

            double num = Double.parseDouble(value);
            return (min <= num && num <= max);
        } catch (NumberFormatException e) {
            // 数値変換に失敗した場合は不正とみなす
            return false;
        }
    }

}
