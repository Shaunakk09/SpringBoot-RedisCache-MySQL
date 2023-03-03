package com.example.SpringBootRedisMySQL.repository;

import com.example.SpringBootRedisMySQL.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAo{
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY = "USER";
    @Override
    public boolean saveUser(User user) {
        try{
            redisTemplate.opsForHash().put(KEY, user.getMid().toString() ,user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> fetchAllUser() {
        List<User> users;
        users = redisTemplate.opsForHash().values(KEY);
        return users;
    }

    @Override
    public User fetchUserByid(Long mid) {
        User user;
        user = (User) redisTemplate.opsForHash().get(KEY,mid.toString());
        return user;
    }

    @Override
    public boolean deleteUser(Long mid) {
        try{
            redisTemplate.opsForHash().delete(KEY,mid.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
