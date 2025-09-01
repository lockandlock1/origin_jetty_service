package service;

import com.google.gson.Gson;
import core.ExternalApiClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    // ✅ 다건 비동기: 10건 동시에 실행하고, 전부 끝난 뒤 List<String> 반환
//    public CompletableFuture<List<String>> searchAllAsync(List<String> queries) {
//        List<CompletableFuture<String>> futures = queries.stream()
//                .map(q -> CompletableFuture.supplyAsync(() -> {
//                    try {
//                        return client.get(SEARCH_URL, Map.of("query", q)); // 동기 호출을 비동기로 감싼다
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }))
//                .collect(Collectors.toList());
//
//        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//        return all.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
//    }

    public List<String> searchAll(List<String> queries) {
        List<CompletableFuture<String>> futures = queries.stream()
                .map(q -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return client.get(SEARCH_URL, Map.of("query", q));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .collect(Collectors.toList()); // Java 11

        CompletableFuture<Void> all =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        List<String> results = all
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();

        return results;
    }


}
