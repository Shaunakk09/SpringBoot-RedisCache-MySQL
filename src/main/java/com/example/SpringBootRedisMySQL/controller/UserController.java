package com.example.SpringBootRedisMySQL.controller;

import com.example.SpringBootRedisMySQL.DAO.UserDAO;
import com.example.SpringBootRedisMySQL.SpringBootRedisMySqlApplication;
import com.example.SpringBootRedisMySQL.model.User;
import com.example.SpringBootRedisMySQL.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(SpringBootRedisMySqlApplication.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserDAO userDAO;

    @PostMapping("/user")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        long now = System.currentTimeMillis();
        user.setDate_time(Long.toString(now));
        User user1 = userDAO.save(user);
        if(userService.UserExistInCache(user.getMid())){
            userService.deleteUserFromCache(user.getMid());
        }
        if(user1 != null) {
            log.info("User created successfully -> " + user1.getFirstName());
            return ResponseEntity.ok("User created successfully");
        }
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user")
    public List<User> fetchAllUser(){
        List<User> users = (List<User>) userDAO.findAll();
        for(User user:users){
            String firstName = user.getFirstName();
            Integer mid = user.getMid();
            log.info("User "+mid.toString()+" -> "+firstName);
        }
        ResponseEntity.ok(users);
        return users;
    }

    @GetMapping("/user/{mid}")
    @CachePut(value = "user",key = "#mid", unless="#result == null")
    public Optional<User> fetchUserbyId(@PathVariable(value = "mid") Integer mid) throws JsonProcessingException {

        if(userService.UserExistInCache(mid) && userService.UserCountZeroCheck(mid)){
            log.info("User exists in cache.");
            Optional<User> UpdatedUser = userService.fetchUserFromCache(mid);
            return  UpdatedUser;
        }

        if(userService.UserExistInCache(mid)){
            if(userService.BlockedUserInCache(mid)){
                log.info("User blocked!!!!!!!");
                if(userService.UserWaitToUnblock(mid)){
                    long now = System.currentTimeMillis();
                    Optional<User> blockedUser = Optional.of(new User(mid,"null","null",0,false,Long.toString(now),1));
                    log.info("User unblocked!!!!!!!");          //user is unblocked after 2 minutes of being blocked
                    return blockedUser;
                }
                else{
                    return userService.fetchUserFromCache(mid);
                }
            }
        }

        if(userDAO.existsById(mid)){
            Optional<User> user = userDAO.findById(mid);
            String firstName = user.get().getFirstName();
            log.info("User "+mid+" -> "+firstName);
            ResponseEntity.ok(user);
        }
        else{
            log.info("User not found in the DataBase.");
            if(userService.UserExistInCache(mid)){
                log.info("Bad user already exists. Incrementing count");
                Optional<User> UpdatedUser = userService.incrementBadUserCount(mid);
                long start = Long.parseLong(UpdatedUser.get().getDate_time());
                long end = System.currentTimeMillis();
                if(UpdatedUser.get().getCounter() < 5){
                    return UpdatedUser;
                }
                else if(UpdatedUser.get().getCounter() == 5 && (end - start) < 60*1000){   //5 requests in less than 1 minute
                    log.info("5 requests for MID in less than 1 minute");
                    long now = System.currentTimeMillis();
                    Optional<User> badUser = Optional.of(new User(mid,"null","null",0,true,Long.toString(now),5));
                    return badUser;
                }
                else if(UpdatedUser.get().getCounter() >= 5 && (end - start) > 60*1000){
                    log.info("5 requests in more than 1 minute so counter reset to 1");
                    long now = System.currentTimeMillis();
                    Optional<User> badUser = Optional.of(new User(mid,"null","null",0,false,Long.toString(now),1));
                    return badUser;
                }
            }
            else{
                log.info("................... NEW BAD USER ............");
                long now = System.currentTimeMillis();
                Optional<User> badUser = Optional.of(new User(mid,"null","null",0,false,Long.toString(now),1));
                return badUser;
            }
        }
        return userDAO.findById(mid);
    }

    @DeleteMapping("/user/{mid}")
    @CacheEvict(value = "user",key = "#mid")
    public ResponseEntity<String> deleteUser(@PathVariable("mid") Integer mid){
        if(userDAO.existsById(mid)) {
            Optional<User> user = userDAO.findById(mid);
            String firstName = user.get().getFirstName();
            userDAO.deleteById(mid);
            log.info("User "+mid.toString()+" Deleted -> "+firstName);
            return ResponseEntity.ok("User Deleted successfully");
        }
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/user")
    @CachePut(value = "user",key = "#user.getMid()")
    public User updateUser(@RequestBody User user){
        return userDAO.save(user);
    }

}
