package dto;

import java.util.List;

public class SearchResponse {

    private Meta meta;

    private List<Document> documents;

    public Meta getMeta() {
        return meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

}
