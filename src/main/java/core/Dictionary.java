package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private final Map<String, String> map;
    private Dictionary(Map<String, String> map) { this.map = map; }

    public static Dictionary load(String path) throws IOException {
        Map<String, String> m = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int idx = line.indexOf('#');
                if (idx > 0) {
                    String word = line.substring(0, idx).trim();
                    String vec  = line.substring(idx + 1).trim();
                    m.put(word, vec);
                }
            }
        }
        return new Dictionary(m);
    }

    public String get(String lowerToken) { return map.get(lowerToken); }
}
