package dto.kakao;

import dto.kakao.Document;
import dto.kakao.Meta;

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
