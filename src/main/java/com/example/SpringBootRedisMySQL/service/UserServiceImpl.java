package com.example.SpringBootRedisMySQL.service;

import com.example.SpringBootRedisMySQL.model.User;
import com.example.SpringBootRedisMySQL.repository.UserDAo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDAo userDAo;
    @Override
    public boolean saveUser(User user) {
        return userDAo.saveUser(user);
    }

    @Override
    public List<User> fetchAllUser() {
        return userDAo.fetchAllUser();
    }

    @Override
    public User fetchUserById(Long mid) {
        return userDAo.fetchUserByid(mid);
    }

    @Override
    public boolean deleteUser(Long mid) {
        return userDAo.deleteUser(mid);
    }
}
