package dto.mock;

public class LexicalTerm {

    private String field;
    private String value;
    private String operator;

    public LexicalTerm(String field, String value, String operator) {
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }
}
