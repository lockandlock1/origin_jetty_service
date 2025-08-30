package core;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import java.nio.charset.StandardCharsets;

public class ExternalApiClient implements AutoCloseable{
    private final HttpClient http;

    public ExternalApiClient() throws Exception {
        this.http = new HttpClient();
        this.http.setConnectTimeout(2000);
        this.http.start();
    }

    public String get(String url) throws Exception {
        ContentResponse res = http.newRequest(url)
                .method(HttpMethod.GET)
                .header(HttpHeader.ACCEPT, "application/json")
                .send();
        return res.getContentAsString();
    }

    public String postJson(String url, String json) throws Exception {
        ContentResponse res = http.newRequest(url)
                .method(HttpMethod.POST)
                .header(HttpHeader.CONTENT_TYPE, "application/json; charset=UTF-8")
                .header(HttpHeader.ACCEPT, "application/json")
                .content(new StringContentProvider("application/json; charset=UTF-8", json, StandardCharsets.UTF_8))
                .send();
        return res.getContentAsString();
    }

//    public List<Model> getModels(Object payload) throws Exception {
//        String raw = client.postJsonRaw(baseUrl + "/models", payload);
//        Type t = new TypeToken<List<Model>>(){}.getType();
//        return gson.fromJson(raw, t);
//    }

    @Override
    public void close() throws Exception {
        http.stop();
    }
}
