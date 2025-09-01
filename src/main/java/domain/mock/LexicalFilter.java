package domain.mock;

import java.util.List;

public class LexicalFilter {

    private String index;
    private List<LexicalTerm> term;

    public LexicalFilter(String index, List<LexicalTerm> term) {
        this.index = index;
        this.term = term;
    }
}
