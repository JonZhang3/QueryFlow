package com.queryflow;

import com.queryflow.reflection.ReflectionUtil;
import com.queryflow.reflection.entity.EntityField;
import com.queryflow.reflection.entity.EntityReflector;
import com.queryflow.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        EntityReflector reflector = ReflectionUtil.forEntityClass(User.class);
        EntityField test = reflector.getField("test");
        System.out.println(test);
        User user = new User();
        test.setValue(user, "123");
        System.out.println(test.getValue(user));
        System.out.println(((Base) user).test);
    }

    static class Base {
        private String test;
    }

    static class User extends Base {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }

}
