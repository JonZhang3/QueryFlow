package com.queryflow.entity;

import com.queryflow.annotation.Column;
import com.queryflow.annotation.Id;
import com.queryflow.annotation.Table;

@Table("user")
public class User {

    @Id
    private long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column("aget")
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", age=" + age +
            '}';
    }
}
