package com.example.domain.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

	private final Class<E> type;

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getCode());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String code = rs.getString(columnName);
		return fromCode(code);
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String code = rs.getString(columnIndex);
		return fromCode(code);
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String code = cs.getString(columnIndex);
		return fromCode(code);
	}

	private E fromCode(String code) {
		return Arrays.stream(type.getEnumConstants()).filter(c -> ((CodeEnum) c).getCode().equals(code)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
	}

}
