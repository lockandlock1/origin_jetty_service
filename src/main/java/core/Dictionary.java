package core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private final Map<String, String> map;

    private Dictionary(Map<String, String> map) {
        this.map = map;
    }

    public static Dictionary load(String resourceName) throws IOException {
        InputStream is = Dictionary.class.getResourceAsStream("/" + resourceName);

        Map<String, String> m = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 필요하면 BOM 처리: if (!line.isEmpty() && line.charAt(0) == '\uFEFF') line = line.substring(1);
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                int idx = line.indexOf('#');
                if (idx > 0) {
                    String word = line.substring(0, idx).trim();
                    String vec  = line.substring(idx + 1).trim();
                    if (!word.isEmpty()) {
                        m.put(word, vec);
                    }
                }
            }
        }
        return new Dictionary(m);
    }

    public String get(String lowerToken) {
        return map.get(lowerToken);
    }
}
