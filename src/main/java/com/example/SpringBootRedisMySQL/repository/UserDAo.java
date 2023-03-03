package com.example.SpringBootRedisMySQL.repository;

import com.example.SpringBootRedisMySQL.model.User;

import java.util.List;

public interface UserDAo {
    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserByid(Long mid);

    boolean deleteUser(Long mid);
}
