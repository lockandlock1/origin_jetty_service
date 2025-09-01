package domain.kakao.dto;

import domain.kakao.Document;
import domain.kakao.Meta;

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
