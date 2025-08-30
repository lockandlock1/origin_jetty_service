package service;

import core.ModelInvoker;
import core.Models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelService {
    private final Models models;
    private final ModelInvoker invoker;

    public ModelService(Models models, ModelInvoker invoker) {
        this.models = models;
        this.invoker = invoker;
    }

    public List<String> infer(String modelname, List<String> preprocessed) {
        Models.ModelInfo mi = models.find(modelname);
        if (mi == null) {
            return Collections.emptyList();
        }

        return preprocessed.stream().map(pp -> {
            try {
                String code = invoker.classify(mi.url, pp);
                return mi.codeToValue.getOrDefault(code, "");
            } catch (Exception e) {
                return "";
            }
        }).collect(Collectors.toList());
    }
}
