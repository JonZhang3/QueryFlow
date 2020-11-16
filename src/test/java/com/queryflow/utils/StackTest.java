package com.queryflow.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class StackTest {

    @Test
    public void test() {
        Stack<String> stack = new Stack<>();
        stack.push("A")
            .push("B")
            .push("C");
        assertEquals("C", stack.peek());
        assertEquals("C", stack.pop());
        stack.push("D");
        assertEquals("ABD", stack.toStr().toString());
    }

}
