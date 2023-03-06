package com.example.SpringBootRedisMySQL.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class User implements Serializable {
    @Id
    private Long mid;
    private String firstName;
    private String lastName;
    private int age;
}
