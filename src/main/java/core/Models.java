package core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Models {
    public static class ModelInfo {
        public final String modelname;
        public final Map<String,String> codeToValue;
        public final String url;
        public ModelInfo(String modelname, Map<String,String> codeToValue, String url) {
            this.modelname = modelname; this.codeToValue = codeToValue; this.url = url;
        }
    }

    private final Map<String, ModelInfo> byName;

    private Models(Map<String, ModelInfo> byName) { this.byName = byName; }

    public static Models load(String path) throws IOException {
        try (Reader r = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            Map<String, ModelInfo> map = new HashMap<>();
            for (JsonElement e : root.getAsJsonArray("models")) {
                JsonObject m = e.getAsJsonObject();
                String name = m.get("modelname").getAsString();
                String url  = m.get("url").getAsString();
                Map<String,String> codeMap = new HashMap<>();
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
