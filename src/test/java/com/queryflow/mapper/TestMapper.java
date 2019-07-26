package com.queryflow.mapper;

import com.queryflow.annotation.*;
import com.queryflow.entity.User;

@Mapper
public abstract class TestMapper {

    @Update("UPDATE user SET username = ${username} WHERE id = ${id}")
    @DataSource("test")
    public abstract void updateUser(@Bind("username") String username,
                                    @Bind("id") String id);

    @Select("SELECT * FROM user WHERE username = ${user.username}")
    public abstract User selectUser(@Bind("user") User user);

}
