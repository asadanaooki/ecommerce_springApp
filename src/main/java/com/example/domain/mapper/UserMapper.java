package com.example.domain.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.model.User;


@Mapper
public interface UserMapper {

	Optional<User> findUserCredentialsByEmail(String email);
}
