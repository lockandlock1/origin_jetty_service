package core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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

//    public Models loadCsv(String resourceName) throws IOException {
//        InputStream is = Models.class.getResourceAsStream("/" + resourceName);
//        if (is == null) throw new FileNotFoundException("Resource not found: " + resourceName);
//
//        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
//            CSVFormat fmt = CSVFormat.DEFAULT.builder()
//                    .setHeader()                // 첫 줄을 헤더로 사용
//                    .setSkipHeaderRecord(true)  // 데이터에서 헤더 건너뛰기
//                    .setIgnoreSurroundingSpaces(true)
//                    .setIgnoreEmptyLines(true)
//                    .build();
//
//            Map<String, String> urlByModel = new HashMap<>();
//            Map<String, Map<String, String>> codeMapByModel = new HashMap<>();
//
//            try (CSVParser parser = new CSVParser(r, fmt)) {
//                for (CSVRecord rec : parser) {
//                    String name  = rec.get("modelname").trim();
//                    String url   = rec.get("url").trim();
//                    String code  = rec.get("code").trim();
//                    String value = rec.get("value").trim();
//
//                    urlByModel.putIfAbsent(name, url); // 동일 모델의 url은 동일하다고 가정
//                    codeMapByModel.computeIfAbsent(name, k -> new HashMap<>())
//                            .put(code, value);
//                }
//            }
//
//            Map<String, Models.ModelInfo> map = new HashMap<>();
//            for (var entry : codeMapByModel.entrySet()) {
//                String name = entry.getKey();
//                Map<String, String> codeMap = entry.getValue();
//                String url = urlByModel.getOrDefault(name, "");
//                map.put(name, new Models.ModelInfo(name, codeMap, url));
//            }
//            return new Models(map);
//        }
//    }

    public String get(String lowerToken) {
        return map.get(lowerToken);
    }
}
