package service;

import core.Preprocessor;

import java.util.List;
import java.util.stream.Collectors;

public class PreprocessService {
    private final Preprocessor preprocessor;
    public PreprocessService(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    public List<String> preprocess(List<String> sentences) {
        return sentences.stream().map(preprocessor::process).collect(Collectors.toList());
    }
}
