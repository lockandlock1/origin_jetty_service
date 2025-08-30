package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private final Set<String> set;
    private StopWords(Set<String> set) { this.set = set; }

    public static StopWords load(String path) throws IOException {
        Set<String> s = new HashSet<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) s.add(line);
            }
        }
        return new StopWords(s);
    }

    public boolean containsVec(String vec) { return set.contains(vec); }
}
