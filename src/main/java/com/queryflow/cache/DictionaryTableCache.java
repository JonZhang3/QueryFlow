package com.queryflow.cache;

import com.queryflow.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DictionaryTableCache {

    private static final Map<String, Map<Object, String>> CACHE = new LinkedHashMap<>();

    private DictionaryTableCache() {
    }

    public static void getAll() {

    }

    public static Map<Object, String> getTable(String tableName) {
        if(Utils.isEmpty(tableName)) {
            return null;
        }
        return CACHE.get(tableName);
    }

    public static String getName(String tableName, Object code) {
        if(Utils.isEmpty(tableName) || code == null) {
            return "";
        }
        Map<Object, String> codes = CACHE.get(tableName);
        String name = "";
        if(codes != null) {
            name = codes.get(code);
        }
        if(name == null) {
            name = "";
        }
        return name;
    }

    public static void putTable(String tableName, Map<Object, String> codes) {
        if(Utils.isEmpty(tableName) || codes == null) {
            return;
        }
        CACHE.put(tableName, codes);
    }

}
