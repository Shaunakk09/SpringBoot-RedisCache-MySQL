package com.example.SpringBootRedisMySQL.service;

import com.example.SpringBootRedisMySQL.model.User;

import java.util.List;

public interface UserService {
    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserById(Integer mid);

    boolean deleteUser(Integer mid);
}
