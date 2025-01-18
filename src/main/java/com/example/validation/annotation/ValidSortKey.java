package com.example.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.validation.validator.SortKeyValidator;

/**
 * フィールドの値が有効なソートキーとして解釈可能かどうかを検証するアノテーションです。
 * <p>
 * 実際の検証ロジックは {@link SortKeyValidator} によって行われます。
 * ソートキーの定義や有効範囲などは実装依存となりますので、{@link SortKeyValidator} 側を参照してください。
 * </p>
 *
 * <ul>
 *   <li>{@link #message()} – 検証エラー時に出力されるメッセージ</li>
 *   <li>{@link #groups()} – バリデーショングループ</li>
 *   <li>{@link #payload()} – 検証失敗時に返却されるメタ情報</li>
 * </ul>
 *
 * @see SortKeyValidator
 */
@Documented
@Constraint(validatedBy = { SortKeyValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortKey {

    /**
     * バリデーションエラー時に表示されるメッセージです。
     *
     * @return エラーメッセージ
     */
    String message() default "SortKeyに変換できる値ではありません。";

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
}
