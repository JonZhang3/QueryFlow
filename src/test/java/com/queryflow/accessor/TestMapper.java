package com.queryflow.accessor;

import com.queryflow.annotation.Update;
import com.queryflow.annotation.Mapper;
import com.queryflow.annotation.Select;
import com.queryflow.annotation.Bind;
import com.queryflow.entity.User;

@Mapper
public abstract class TestMapper {

    @Update("INSERT INTO user VALUES (${user.id}, ${user.username}, ${user.password}, ${user.age})")
    public abstract void insert(@Bind("user") User user);

}
