package core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Models {
    public static class ModelInfo {
        public final String modelname;
        public final Map<String,String> codeToValue;
        public final String url;
        public ModelInfo(String modelname, Map<String,String> codeToValue, String url) {
            this.modelname = modelname;
            this.codeToValue = codeToValue;
            this.url = url;
        }
    }

    private final Map<String, ModelInfo> byName;

    public Models(Map<String, ModelInfo> byName) {
        this.byName = byName;
    }

//
//    public static Models load(String resourceName) throws IOException {
//        // 예: resourceName = "models.json" 또는 "config/models.json"
//        InputStream is = Models.class.getResourceAsStream("/" + resourceName);
//
//
//        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
//            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
//
//            Map<String, ModelInfo> map = new HashMap<>();
//            var modelsArr = root.getAsJsonArray("models");
//            if (modelsArr == null) throw new IOException("JSON key 'models' is missing or not an array");
//
//            for (JsonElement e : modelsArr) {
//                JsonObject m = e.getAsJsonObject();
//                String name = m.get("modelname").getAsString();
//                String url  = m.get("url").getAsString();
//
//                Map<String, String> codeMap = new HashMap<>();
//                var classesArr = m.getAsJsonArray("classes");
//                if (classesArr != null) {
//                    for (JsonElement c : classesArr) {
//                        JsonObject co = c.getAsJsonObject();
//                        codeMap.put(co.get("code").getAsString(), co.get("value").getAsString());
//                    }
//                }
//                map.put(name, new ModelInfo(name, codeMap, url));
//            }
//            return new Models(map);
//        }
//    }

    public static Models load(String relativePath) throws IOException {
        try (Reader r = new InputStreamReader(new FileInputStream(relativePath), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();

            Map<String, ModelInfo> map = new HashMap<>();
            for (JsonElement e : root.getAsJsonArray("models")) {
                JsonObject m = e.getAsJsonObject();
                String name = m.get("modelname").getAsString();
                String url  = m.get("url").getAsString();

                Map<String, String> codeMap = new HashMap<>();
                for (JsonElement c : m.getAsJsonArray("classes")) {
                    JsonObject co = c.getAsJsonObject();
                    codeMap.put(co.get("code").getAsString(), co.get("value").getAsString());
                }

                map.put(name, new ModelInfo(name, codeMap, url));
            }
            return new Models(map);
        }
    }



    public ModelInfo find(String name) { return byName.get(name); }
}
