package com.example.SpringBootRedisMySQL.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "userDB")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User implements Serializable {
    @Column(name = "mid")
    @Id      //primary key in the table
    private Integer mid;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "age")
    private int age;
    @Column(name = "blacklisted")
    private boolean blacklisted;
    @Column(name = "date_time")
    private String date_time;
    @Column(name = "counter")
    private int counter;

    public User(Integer mid, String firstName, String lastName, int age, boolean blacklisted, String now, int counter) {
        this.mid = mid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.blacklisted = blacklisted;
        this.date_time = now;
        this.counter = counter;
    }
}
