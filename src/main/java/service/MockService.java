package service;

import com.google.gson.Gson;
import core.ExternalApiClient;
import dto.mock.MockRequest;

public class MockService {

    private static final String MOCK_URL = "http://localhost:8000/mock";

    private final Gson gson = new Gson();

    private final ExternalApiClient client;

    public MockService(ExternalApiClient client) {
        this.client = client;
    }

    public String mock(MockRequest request) throws Exception {
        String payload = gson.toJson(request);

        return client.postJson(MOCK_URL, payload);
    }
}
