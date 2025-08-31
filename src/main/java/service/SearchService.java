package service;

import com.google.gson.Gson;
import core.ExternalApiClient;
import dto.SearchResponse;

import java.util.Map;

public class SearchService {

    private static final String SEARCH_URL = "https://dapi.kakao.com/v2/search/web";

    private final Gson gson = new Gson();

    private final ExternalApiClient client;

    public SearchService(ExternalApiClient client) {
        this.client = client;
    }

    public String search(String query) throws Exception {
        Map<String, String> queryParameters = Map.of("query", query);

        return client.get(SEARCH_URL, queryParameters);
    }

}
