package com.queryflow;

import com.queryflow.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, Field> fields = new HashMap<>();
        Utils.getFields(User.class, fields);
        System.out.println(fields);
    }

    static class Base {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    static class User extends Base {
        private String username;
        private String test;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

}
