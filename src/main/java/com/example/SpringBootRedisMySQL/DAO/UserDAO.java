package com.example.SpringBootRedisMySQL.DAO;

import com.example.SpringBootRedisMySQL.model.User;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface UserDAO extends CrudRepository<User, Integer> {
}
