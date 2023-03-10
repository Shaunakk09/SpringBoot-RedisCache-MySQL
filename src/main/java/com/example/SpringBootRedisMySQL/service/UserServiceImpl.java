package com.example.SpringBootRedisMySQL.service;

import com.example.SpringBootRedisMySQL.SpringBootRedisMySqlApplication;
import com.example.SpringBootRedisMySQL.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(SpringBootRedisMySqlApplication.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public String ValueOfRedisCacheKey(String userKey){
        ValueOperations<String, Object> valueOperations;
        valueOperations = redisTemplate.opsForValue();
        String user = (String) valueOperations.get(userKey);
        return user;
    }

    @Override
    public boolean UserExistInCache(Integer mid) {
        Set<String> redisKeys = redisTemplate.keys("*");
        String userKey = "user::" + mid.toString();
        for (String key : redisKeys) {
            if(key.equals(userKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<User> incrementBadUserCount(Integer mid) throws JsonProcessingException {
        String userKey = "user::"+mid.toString();
        String user = ValueOfRedisCacheKey(userKey);
        log.info(user);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(user);
        String className = node.get("@class").asText();
        Integer Mid = node.get("mid").asInt();
        String firstName = node.get("firstName").asText();
        String lastName = node.get("lastName").asText();
        Integer age = node.get("age").asInt();
        Boolean blacklisted = node.get("blacklisted").asBoolean();
        long now = System.currentTimeMillis();
        Integer counter = node.get("counter").asInt()+1;
        Optional<User> UpdatedUser = Optional.of(new User(Mid, firstName, lastName, age, blacklisted, Long.toString(now), counter));
        return UpdatedUser;
    }

    @Override
    public Optional<User> fetchUserFromCache(Integer mid) throws JsonProcessingException {
        String userKey = "user::"+mid.toString();
        String user = ValueOfRedisCacheKey(userKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(user);
        String className = node.get("@class").asText();
        Integer Mid = node.get("mid").asInt();
        String firstName = node.get("firstName").asText();
        String lastName = node.get("lastName").asText();
        Integer age = node.get("age").asInt();
        Boolean blacklisted = node.get("blacklisted").asBoolean();
        String time = node.get("date_time").asText();
        Integer counter = node.get("counter").asInt();
        Optional<User> UpdatedUser = Optional.of(new User(Mid, firstName, lastName, age, blacklisted, time, counter));
        return UpdatedUser;
    }

    @Override
    public void deleteUserFromCache(Integer mid) {
        String userKey = "user::"+mid.toString();
        redisTemplate.delete(userKey);
    }

    @Override
    public boolean BlockedUserInCache(Integer mid) throws JsonProcessingException {
        String userKey = "user::"+mid.toString();
        String user = ValueOfRedisCacheKey(userKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(user);
        Boolean blacklisted = node.get("blacklisted").asBoolean();
        return blacklisted;
    }

    @Override
    public boolean UserWaitToUnblock(Integer mid) throws JsonProcessingException {
        String userKey = "user::"+mid.toString();
        String user = ValueOfRedisCacheKey(userKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(user);
        String startTime = node.get("date_time").asText();
        long l=Long.parseLong(startTime);
        long now = System.currentTimeMillis();
        if((now - l) >= 2*60*1000) return true;
        return false;
    }

    @Override
    public boolean UserCountZeroCheck(Integer mid) throws JsonProcessingException {
        String userKey = "user::"+mid.toString();
        String user = ValueOfRedisCacheKey(userKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(user);
        Integer counter = node.get("counter").asInt();
        if(counter == 0) return true;
        return false;
    }

}
