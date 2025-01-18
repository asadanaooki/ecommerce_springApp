package com.example.validation.validator;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.domain.model.enums.SortKey;
import com.example.validation.annotation.ValidSortKey;

/**
 * {@link ValidSortKey} アノテーションが付与された文字列が、
 * {@link SortKey} 列挙型のいずれかの値と一致するかを検証するバリデータです。
 * <p>
 * 文字列が <code>null</code> または <code>SortKey</code> の定義に含まれない場合は不正とみなし、
 * <code>false</code> を返却します。
 * </p>
 *
 * @see ValidSortKey
 * @see SortKey
 */
public class SortKeyValidator implements ConstraintValidator<ValidSortKey, String> {

    /**
     * フィールドの値（<code>value</code>）が {@link SortKey#name()} のいずれかに該当するかをチェックします。
     * <p>
     * <ul>
     *   <li>該当すれば <code>true</code>、該当しなければ <code>false</code> を返却</li>
     *   <li><code>null</code> の場合もマッチしないため <code>false</code> を返却</li>
     * </ul>
     * </p>
     *
     * @param value   検証対象の文字列
     * @param context バリデーションコンテキスト
     * @return <code>value</code> が <code>SortKey</code> のいずれかの <code>name()</code> に一致する場合 <code>true</code>
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(SortKey.values())
                     .anyMatch(s -> s.name().equals(value));
    }
}
