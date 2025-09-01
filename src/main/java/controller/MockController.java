package controller;

import com.google.gson.Gson;
import domain.common.ApiResponse;
import domain.mock.dto.MockRequest;
import domain.mock.dto.MockResponse;
import service.MockService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class MockController extends HttpServlet {

    private final Gson gson = new Gson();

    private final MockService mockService;

    public MockController(MockService mockService) {
        this.mockService = mockService;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("test");
        MockRequest input;
        try (Reader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            input = gson.fromJson(reader, MockRequest.class);
            String extResponse = mockService.mock(input);
            MockResponse response = gson.fromJson(extResponse, MockResponse.class);
            writeJson(resp, 200, ApiResponse.success(response));
        } catch (Exception e) {
            System.out.println(e);
            writeJson(resp, 500, ApiResponse.error("error", "fucking error"));
        }
    }


    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(body));
    }
}
