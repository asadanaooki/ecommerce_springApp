package com.example.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.validation.validator.StringIntegerRangeValidator;

/**
 * 文字列表現の数値が、指定した最小値（{@link #min()}）以上、最大値（{@link #max()}）以下かを検証するためのアノテーションです。
 * <p>
 * このアノテーションを付与したフィールドの値が文字列であり、整数値に変換可能であることを前提とします。
 * 実際の検証ロジックは {@link StringIntegerRangeValidator} によって実行されます。
 * </p>
 *
 * <ul>
 *   <li>{@link #min()} – 許容する整数値の最小値</li>
 *   <li>{@link #max()} – 許容する整数値の最大値</li>
 *   <li>{@link #message()} – 検証エラー時に出力されるメッセージ</li>
 *   <li>{@link #groups()} – バリデーショングループ</li>
 *   <li>{@link #payload()} – 検証失敗時に返却されるメタ情報</li>
 * </ul>
 *
 * <pre><code>
 * // 使用例:
 * public class ExampleForm {
 *
 *   &#64;StringIntegerRange(
 *     min = 0,
 *     max = 100,
 *     message = "0〜100の範囲で入力してください。"
 *   )
 *   private String quantity;
 *
 *   // ゲッター/セッター など
 * }
 * </code></pre>
 *
 * @see StringIntegerRangeValidator
 */
@Documented
@Constraint(validatedBy = { StringIntegerRangeValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringIntegerRange {

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
     * 許容する整数値の最小値です。
     *
     * @return 最小値
     */
    int min();

    /**
     * 許容する整数値の最大値です。
     *
     * @return 最大値
     */
    int max();

}
