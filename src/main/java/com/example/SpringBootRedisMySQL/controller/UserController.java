package com.example.SpringBootRedisMySQL.controller;

import com.example.SpringBootRedisMySQL.model.User;
import com.example.SpringBootRedisMySQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        boolean result = userService.saveUser(user);
        if(result) return  ResponseEntity.ok("User created successfully");
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> fetchAllUser() {
        List<User> users;
        users = userService.fetchAllUser();
        return  ResponseEntity.ok(users);
    }

    @GetMapping("/user/{mid}")
    public ResponseEntity<User> fetchUserbyId(@PathVariable("mid") Long mid){
        User user;
        user = userService.fetchUserById(mid);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/user/{mid}")
    public ResponseEntity<String> deleteUser(@PathVariable("mid") Long mid){
        boolean result = userService.deleteUser(mid);
        if(result) return  ResponseEntity.ok("User Deleted successfully");
        else  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
