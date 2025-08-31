package dto.mock;

public class Query {
    private String role;
    private String content;

    public Query(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}
