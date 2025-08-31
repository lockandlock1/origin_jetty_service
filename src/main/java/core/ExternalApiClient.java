package core;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExternalApiClient implements AutoCloseable {
    private final HttpClient http;

    public ExternalApiClient() throws Exception {
        SslContextFactory.Client ssl = new SslContextFactory.Client();
        this.http = new HttpClient(ssl);
        this.http.setConnectTimeout(2000);
        this.http.start();
    }

    //    KaKao Open API GET 호출
    public String get(String url, Map<String, String> queryParameters) throws Exception {
        String requestUrl = buildUrl(url, queryParameters);

        ContentResponse res = http.newRequest(requestUrl)
                .method(HttpMethod.GET)
                .header(HttpHeader.ACCEPT, "application/json")
                .header(HttpHeader.AUTHORIZATION, "KakaoAK " + "16d5f6a7bef175b8649df70e9249e996")
                .send();
        return res.getContentAsString();
    }


    private String buildUrl(String baseUrl, Map<String, String> params) {
        if (params == null || params.isEmpty()) return baseUrl;

        StringBuilder sb = new StringBuilder(baseUrl).append('?');
        boolean first = true;
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (!first) {
                sb.append('&');
            }
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(String.valueOf(e.getValue()), StandardCharsets.UTF_8));
            first = false;
        }
        return sb.toString();
    }

    // 2) 객체 → JSON 직렬화 → POST
    public String postJson(String url, String json) throws Exception {
        ContentResponse res = http.newRequest(url)
                .method(HttpMethod.POST)
                .header(HttpHeader.CONTENT_TYPE, "application/json; charset=UTF-8")
                .header(HttpHeader.ACCEPT, "application/json")
                .content(new StringContentProvider("application/json; charset=UTF-8", json, java.nio.charset.StandardCharsets.UTF_8))
                .send();

        int status = res.getStatus();
        if (status < 200 || status >= 300) {
            throw new IllegalStateException("HTTP " + status + ": " + res.getContentAsString());
        }
        return res.getContentAsString();
    }

    @Override
    public void close() throws Exception {
        http.stop();
    }
}
