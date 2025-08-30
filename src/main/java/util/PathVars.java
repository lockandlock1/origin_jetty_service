package util;

import java.util.HashMap;
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
}
