package domain.mock.dto;

import domain.mock.LexicalFilter;
import domain.mock.Query;

import java.util.List;

public class MockRequest {

    private List<Query> queries;
    private String model;
    private List<LexicalFilter> filter;
}
