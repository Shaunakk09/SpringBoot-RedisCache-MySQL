package com.example.SpringBootRedisMySQL.controller;

import com.example.SpringBootRedisMySQL.SpringBootRedisMySqlApplication;
import com.example.SpringBootRedisMySQL.model.User;
import com.example.SpringBootRedisMySQL.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(SpringBootRedisMySqlApplication.class);
    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        String firstName = user.getFirstName();
        boolean result = userService.saveUser(user);
        if(result) {
            log.info("User created successfully -> " + firstName);
            return ResponseEntity.ok("User created successfully");
        }
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user")
    public List<User> fetchAllUser() {
        List<User> users;
        users = userService.fetchAllUser();
        for(User user:users){
            String firstName = user.getFirstName();
            Long mid = user.getMid();
            log.info("User "+mid.toString()+" -> "+firstName);
        }
        ResponseEntity.ok(users);
        return users;
    }

    @GetMapping("/user/{mid}")
    @Cacheable(value = "user",key = "#mid")
    public User fetchUserbyId(@PathVariable(value = "mid") Long mid){
        User user;
        user = userService.fetchUserById(mid);
        String firstName = user.getFirstName();
        log.info("User "+mid+" -> "+firstName);
        ResponseEntity.ok(user);
        return userService.fetchUserById(mid);
    }
    @DeleteMapping("/user/{mid}")
    @CacheEvict(value = "user",key = "#mid")
    public ResponseEntity<String> deleteUser(@PathVariable("mid") Long mid){
        User user = userService.fetchUserById(mid);
        String firstName = user.getFirstName();
        boolean result = userService.deleteUser(mid);
        if(result) {
            log.info("User "+mid.toString()+" Deleted -> "+firstName);
            return  ResponseEntity.ok("User Deleted successfully");
        }
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/user")
    @CachePut(value = "user",key = "#user.getMid()")
    public User updateUser(@RequestBody User user){
        User oldUser = fetchUserbyId(user.getMid());
        if(oldUser != null){
            String oldUserName = oldUser.getFirstName();
            deleteUser(oldUser.getMid());
            saveUser(user);
            ResponseEntity.ok(user);
            log.info("Old user "+oldUserName+" Deleted. New user "+user.getFirstName());
            return user;
        }
        saveUser(user);
        return user;
    }
}
