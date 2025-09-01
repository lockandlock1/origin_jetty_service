package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.kice.ApiRequest;
import service.ModelService;
import service.PreprocessService;
import util.PathVars;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InferenceController extends HttpServlet {
    private final PreprocessService preprocessService;
    private final ModelService modelService;
    private final Gson gson = new Gson();

    public InferenceController(PreprocessService preprocessService, ModelService modelService) {
        this.preprocessService = preprocessService;
        this.modelService = modelService;
    }


//    PathVarialbe 예시
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // "/42/rag/abc123"

        Map<String, String> params = PathVars.match("/deploys/{deployId}/rag/{ragPipeline}", path);
        if (params != null) {
            long deployId = Long.parseLong(params.get("deployId"));
            String ragPipeline = params.get("ragPipeline");

            writeJson(resp, 200, Map.of(
                    "deployId", deployId,
                    "ragPipeline", ragPipeline,
                    "status", "OK"
            ));
            return;
        }

        writeJson(resp, 404, Map.of("message", "Not Found"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try (Reader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            ApiRequest input = gson.fromJson(reader, ApiRequest.class);

            if (input == null || input.getModelname() == null || input.getQueries() == null) {
                writeResults(resp, Collections.emptyList());
                return;
            }

            // 전처리 & 모델 추론
            List<String> preprocessed = preprocessService.preprocess(input.getQueries());
            List<String> results = modelService.infer(input.getModelname(), preprocessed);

            writeResults(resp, results);
        } catch (Exception e) {
            writeResults(resp, Collections.emptyList());
        }
    }

    private void writeResults(HttpServletResponse resp, List<String> results) throws IOException {
        JsonObject out = new JsonObject();
        out.add("results", gson.toJsonTree(results));
        resp.setStatus(200); // 시험 요구: 항상 200 OK
        try (Writer w = resp.getWriter()) {
            gson.toJson(out, w);
        }
    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(body));
    }

}
