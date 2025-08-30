package dto;

import java.util.List;

public class ApiRequest {
    private String modelname;
    private List<String> queries;

    public String getModelname() { return modelname; }
    public void setModelname(String modelname) { this.modelname = modelname; }

    public List<String> getQueries() { return queries; }
    public void setQueries(List<String> queries) { this.queries = queries; }
}
