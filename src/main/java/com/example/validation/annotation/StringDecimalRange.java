package com.example.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.validation.validator.StringDecimalRangeValidator;

/**
 * 文字列表現の数値が、指定した最小値（{@link #min()}）以上、最大値（{@link #max()}）以下かを検証するためのアノテーションです。
 * <p>
 * このアノテーションを付与したフィールドの値が文字列であり、数値に変換可能であることを前提とします。
 * 検証時には {@link StringDecimalRangeValidator} によって、数値範囲が適切かどうかをチェックします。
 * </p>
 *
 * <ul>
 *   <li>{@link #min()} – 許容する数値の最小値</li>
 *   <li>{@link #max()} – 許容する数値の最大値</li>
 *   <li>{@link #message()} – 検証エラー時に出力されるメッセージ</li>
 *   <li>{@link #groups()} – バリデーショングループ</li>
 *   <li>{@link #payload()} – 検証失敗時に返却されるメタ情報</li>
 * </ul>
 *
 * @see StringDecimalRangeValidator
 */
@Documented
@Constraint(validatedBy = { StringDecimalRangeValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringDecimalRange {

    /**
     * バリデーションエラー時に表示されるメッセージです。
     *
     * @return エラーメッセージ
     */
    String message() default "値が範囲外です。";

    /**
     * バリデーショングループを指定します。
     *
     * @return バリデーショングループのクラス配列
     */
    Class<?>[] groups() default {};

    /**
     * 検証失敗時に返却されるメタ情報を指定します。
     *
     * @return Payload のクラス配列
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 許容する数値の最小値です。
     *
     * @return 最小値
     */
    double min();

    /**
     * 許容する数値の最大値です。
     *
     * @return 最大値
     */
    double max();

}
