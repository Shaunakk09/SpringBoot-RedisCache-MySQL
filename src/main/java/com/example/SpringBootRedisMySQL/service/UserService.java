package com.example.SpringBootRedisMySQL.service;

import com.example.SpringBootRedisMySQL.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface UserService {

    boolean UserExistInCache(Integer mid);

    Optional<User> incrementBadUserCount(Integer mid) throws JsonProcessingException;

    Optional<User> fetchUserFromCache(Integer mid) throws JsonProcessingException;

    void deleteUserFromCache(Integer mid);

    boolean BlockedUserInCache(Integer mid) throws JsonProcessingException;

    boolean UserWaitToUnblock(Integer mid) throws JsonProcessingException;

    boolean UserCountZeroCheck(Integer mid) throws JsonProcessingException;
}
