package com.queryflow.cache;

import com.queryflow.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DictionaryTableCache {

    private final Map<String, Map<Object, String>> CACHE = new LinkedHashMap<>();

    public DictionaryTableCache() {
    }

    public Map<String, Map<Object, String>> getAll() {
        Map<String, Map<Object, String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, Map<Object, String>> entry : CACHE.entrySet()) {
            String tableName = entry.getKey();
            Map<Object, String> codes = entry.getValue();
            result.put(tableName, codes == null ? null : new LinkedHashMap<>(codes));
        }
        return result;
    }

    public Map<Object, String> getTable(String tableName) {
        if(Utils.isEmpty(tableName)) {
            return null;
        }
        Map<Object, String> codes = CACHE.get(tableName);
        return codes == null ? null : new LinkedHashMap<>(codes);
    }

    public String getName(String tableName, Object code) {
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

    public void putTable(String tableName, Map<Object, String> codes) {
        if(Utils.isEmpty(tableName) || codes == null) {
            return;
        }
        CACHE.put(tableName, new LinkedHashMap<>(codes));
    }

}
