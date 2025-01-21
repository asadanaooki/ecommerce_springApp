package com.example.util.Redis;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@ExtendWith(MockitoExtension.class)
class RedisUtilTest {

    @Mock
    StringRedisTemplate template;

    @Mock
    SetOperations<String, String> setOperations;

    @InjectMocks
    RedisUtil redisUtil;

    @BeforeEach
    void setup() {
        when(template.opsForSet()).thenReturn(setOperations);
    }

    @Nested
    class isMemberOfSet {
        String key = "myKey";
        String value = "myValue";

        @Test
        void isMemberOfSet() {
            // arrange
            when(setOperations.isMember(key, value)).thenReturn(true);
            
            // act
           boolean result = redisUtil.isMemberOfSet(key, value);
            
            // assert
            assertThat(result).isTrue();
        }
        
        @Test
        void isNotMemberOfSet() {
            // arrange
            when(setOperations.isMember(key, value)).thenReturn(false);
            
            // act
           boolean result = redisUtil.isMemberOfSet(key, value);
            
            // assert
            assertThat(result).isFalse();
        }
    }

}
