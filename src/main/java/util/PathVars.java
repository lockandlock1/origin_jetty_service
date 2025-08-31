package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathVars {
    private PathVars() {}

    /**
     * 템플릿과 실제 path를 비교해서 {변수명}을 Map으로 추출한다.
     * ex) match("/deploys/{deployId}/rag/{ragPipeline}", "/deploys/42/rag/abc123")
     * => { deployId=42, ragPipeline=abc123 }
     */
    public static Map<String, String> match(String template, String path) {
        if (path == null) return null;

        // {변수명} → (?<변수명>[^/]+) 로 치환
        String regex = Pattern.quote(template)
                .replace("\\{", "{").replace("}", "}") // quote 처리 복원
                .replaceAll("\\{([a-zA-Z0-9_]+)\\}", "(?<$1>[^/]+)");
        regex = "^" + regex + "/?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) return null;

        // 결과 담을 Map
        Map<String, String> resultMap = new HashMap<>();

        // 템플릿에서 변수명 추출
        Matcher groupMatcher = Pattern.compile("\\{([a-zA-Z0-9_]+)\\}").matcher(template);
        while (groupMatcher.find()) {
            String name = groupMatcher.group(1);
            resultMap.put(name, matcher.group(name));
        }
        return resultMap;
    }

    public static Map<String, String> extractQueryParams(String url) {
        if (url == null) {
            return Collections.emptyMap();
        }
        int q = url.indexOf('?');
        if (q < 0 || q == url.length() - 1) {
            return Collections.emptyMap();
        }

        String qs = url.substring(q + 1);
        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : qs.split("&")) {
            if (pair.isEmpty()) continue;
            int i = pair.indexOf('=');
            String k = i >= 0 ? pair.substring(0, i) : pair;
            String v = i >= 0 ? pair.substring(i + 1) : "";
            map.put(
                    URLDecoder.decode(k, StandardCharsets.UTF_8),
                    URLDecoder.decode(v, StandardCharsets.UTF_8)
            );
        }
        return map;
    }

    // 원하는 키만 바로 꺼내고 싶으면
    public static String getQueryParam(String url, String key) {
        return extractQueryParams(url).get(key);
    }

    // 코드 예시
//    String url = "https://dapi.kakao.com/v2/search/web?query=%EC%9D%B4%ED%9A%A8%EB%A6%AC&size=10";
//    String query = getQueryParam(url, "query"); // "이효리"
//    Map<String,String> all = extractQueryParams(url); // {query=이효리, size=10}

}
