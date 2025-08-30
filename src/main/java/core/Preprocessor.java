package core;

import java.util.ArrayList;
import java.util.List;

public class Preprocessor {
    private final Dictionary dict;
    private final StopWords stop;

    public Preprocessor(Dictionary dict, StopWords stop) {
        this.dict = dict;
        this.stop = stop;
    }

    /** 공백 토큰화 → 소문자 → 사전매핑 → 불용어(임베딩값 기준) 제거 → 공백 join */
    public String process(String sentence) {
        if (sentence == null || sentence.isEmpty())  {
            return "";
        }
        String[] tokens = sentence.split("\\s+");
        List<String> kept = new ArrayList<>();
        for (String t : tokens) {
            String key = t.toLowerCase();
            String vec = dict.get(key);
            if (vec != null && !stop.containsVec(vec)) {
                kept.add(vec);
            }
        }
        return String.join(" ", kept);
    }
}
