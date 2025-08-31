package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dto.ApiResponse;
import service.SearchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KeywordSearchController extends HttpServlet {

    private final SearchService searchService;

    private final Gson gson = new Gson();
    public KeywordSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");

        String search;
        try {
            search = searchService.search(query);

            // 가장 단순

//            String raw = client.get(kakaoUrl, Map.of("query", query));
//            resp.setStatus(200);
//            resp.setContentType("application/json; charset=UTF-8");
//            resp.getWriter().write(raw);

//            pojo 필요시 -> 응답 객체를 다뤄야 한다면
//            SearchResponse data = gson.fromJson(raw, SearchResponse.class);
//// 가공/검증/필터링이 필요할 때만 POJO 사용
//            writeJson(resp, 200, ApiResponse.success(data));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JsonElement payload = JsonParser.parseString(search);
        writeJson(resp, 200, ApiResponse.success(payload));

    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(body));
    }
}
