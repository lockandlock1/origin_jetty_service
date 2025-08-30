package core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ModelInvoker {
    private final HttpClient client;
    private final Gson gson = new Gson();

    public ModelInvoker() throws Exception {
        this.client = new HttpClient();
        this.client.setConnectTimeout(10_000);
        this.client.start();
    }

    /**
     * 문항 스펙: {"query":"<전처리된 문장>"} → {"result":"<분류코드>"}
     */
    public String classify(String url, String preprocessed) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("query", preprocessed);

        ContentResponse res = client.newRequest(url)
                .method(HttpMethod.POST)
                .content(new StringContentProvider("application/json", body.toString(), StandardCharsets.UTF_8))
                .timeout(10_000, TimeUnit.MILLISECONDS)
                .send();

        if (res.getStatus() != 200) {
            throw new IllegalStateException("Model HTTP " + res.getStatus());
        }
        JsonObject json = gson.fromJson(res.getContentAsString(), JsonObject.class);
        if (!json.has("result")) {
            throw new IllegalStateException("No result field");
        }
        return json.get("result").getAsString();
    }
}
