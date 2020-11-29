package com.queryflow.utils;

import static org.junit.Assert.*;

import com.queryflow.entity.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIsPrimitiveOrWrapper() {
        assertTrue(Utils.isPrimitiveOrWrapper(Integer.TYPE));
        assertTrue(Utils.isPrimitiveOrWrapper(Float.class));
        assertFalse(Utils.isPrimitiveOrWrapper(String.class));
    }

    @Test
    public void testInstantiate() {
        User user = Utils.instantiate(User.class);
        assertNotNull(user);
    }

    @Test
    public void testGetDefaultConstructor() {
        assertNull(Utils.getDefaultConstructor(null));
        assertNotNull(Utils.getDefaultConstructor(User.class));
    }

    @Test
    public void testToArray() {
        List<String> testList = new ArrayList<String>(){{
            add("A");
            add("B");
            add("C");
            add("D");
        }};
        String[] expectedArray = {"A", "B", "C", "D"};

        assertNull(Utils.toArray(null));

        assertArrayEquals(expectedArray, Utils.toArray(expectedArray));

        assertArrayEquals(expectedArray, Utils.toArray(testList));

        assertArrayEquals(new String[]{"A"}, Utils.toArray("A"));

        List<Object[]> testObjs = new ArrayList<>();
        testObjs.add(new Object[]{1, "A"});
        testObjs.add(new Object[]{2, "B"});
        Object[][] expectedObjs = new Object[][]{
            new Object[]{1, "A"},
            new Object[]{2, "B"}
        };

        assertArrayEquals(expectedObjs, Utils.toArray(testObjs));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Utils.isEmpty(""));
        assertFalse(Utils.isEmpty("a"));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(Utils.isNotEmpty(""));
        assertTrue(Utils.isNotEmpty("a"));
    }

    @Test
    public void testIsBlank() {
        assertTrue(Utils.isBlank(null));
        assertTrue(Utils.isBlank(""));
        assertTrue(Utils.isBlank("   "));
        assertFalse(Utils.isBlank(" a "));
    }

    @Test
    public void testCamelCaseToSnake() {
        assertEquals("user_name", Utils.camelCaseToSnake("userName"));
        assertEquals("abc_a_b", Utils.camelCaseToSnake("abcAB"));
    }

    @Test
    public void testJoin() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        assertEquals("a!,b!,c!,d!", Utils.join(",", list, "!"));
        assertEquals("abcd", Utils.join("", list));
    }

    @Test
    public void test() throws IOException, URISyntaxException {
        System.out.println(Utils.findHomeDir());
    }

}
