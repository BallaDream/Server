package com.BallaDream.BallaDream.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//Redis 서버에 key 는 이메일주소고 value 는 난수인 데이터 (인증 코드)
@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스
    private final RedisTemplate<String, Object> redisListTemplate;
    private final ObjectMapper objectMapper;

    public String getData(String key){//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
    public void setData(String key,String value){//지정된 키(key)에 값을 저장하는 메서드
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    //여러개의 value 를 가진 데이터를 가져옴 (채팅방 참여 인원 관리)
    public List<String> getMultiData(String key){//지정된 키(key)에 해당하는 데이터를 Redis 에서 가져오는 메서드
        RedisOperations<String, Object> operations = redisListTemplate.opsForList().getOperations();
        List<Object> result = operations.opsForList().range(key, 0, -1);
        if (result == null) {
            return new ArrayList<>();
        }
        return result.stream().map(String::valueOf).toList();
    }
    //추가로 value 에 값을 넣을 때 사용 (채팅방 참여 인원 관리)
    public void setMultiData(String key, String value){//지정된 키(key)에 값을 저장하는 메서드
        RedisOperations<String, Object> operations = redisListTemplate.opsForList().getOperations();
        operations.opsForList().rightPush(key, value);
    }

    public void setDataExpire(String key, String value, long duration){//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        Duration expireDuration=Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }
    public void deleteData(String key){//지정된 키(key)에 해당하는 데이터를 Redis 에서 삭제하는 메서드
        redisTemplate.delete(key);
        redisListTemplate.delete(key);
    }
}
