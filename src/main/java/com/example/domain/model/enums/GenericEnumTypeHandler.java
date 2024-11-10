package com.example.domain.model.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import lombok.AllArgsConstructor;

/**
 * MyBatis用の汎用的な列挙型ハンドラー。
 * <p>
 * データベースとJava列挙型間でのマッピングを行うための型ハンドラーです。 列挙型は {@code HasCode}
 * インターフェースを実装している必要があります。
 * </p>
 * 
 * @param <E> {@code Enum<E>} 型で、かつ {@code HasCode} インターフェースを実装している列挙型
 */
@AllArgsConstructor
public class GenericEnumTypeHandler<E extends Enum<E> & HasCode> extends BaseTypeHandler<E> {

	/**
	 * 列挙型のクラス情報
	 */
	private final Class<E> type;

	/**
	 * 非NULLの値を設定
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getCode());
	}

	/**
	 * データベースから結果を取得し、列挙型にマッピング
	 */
	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String code = rs.getString(columnName);
		return fromCode(code);
	}

	/**
	 * データベースから結果を取得し、列挙型にマッピング
	 */
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String code = rs.getString(columnIndex);
		return fromCode(code);
	}

	/**
	 * データベースから結果を取得し、列挙型にマッピング
	 */
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String code = cs.getString(columnIndex);
		return fromCode(code);
	}

	/**
	 * 列挙型に対応するコードから列挙型インスタンスを取得
	 * @param code コード
	 * @return 列挙型インスタンス
	 */
	private E fromCode(String code) {
		return Arrays.stream(type.getEnumConstants()).filter(c -> ((HasCode) c).getCode().equals(code)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
	}

}
