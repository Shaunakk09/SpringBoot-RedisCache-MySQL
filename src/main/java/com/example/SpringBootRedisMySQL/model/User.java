package com.example.SpringBootRedisMySQL.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Long mid;
    private String firstName;
    private String lastName;
    private int age;
}
