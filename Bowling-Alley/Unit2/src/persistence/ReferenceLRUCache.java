package persistence;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ReferenceLRUCache {
    private static LinkedHashMap<String, String> cache = new LinkedHashMap<>();
    private static int capacity = 20;

    public static String getEntry(String key) {
        if (cache.containsKey(key)) {
            String entry = cache.remove(key);
            cache.put(key, entry);
            return cache.get(key);
        } else {
            return putEntry(key);
        }
    }

    private static String putEntry(String key) {
        if (cache.size() == capacity) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }
        try {
            String value = (String) ReferenceDb.getEntry(key);
            cache.put(key, String.valueOf(value));
            return value;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
