package dto;

import java.util.List;

public class ResultsDto {
    private List<String> results;
    public ResultsDto(List<String> results) { this.results = results; }
    public List<String> getResults() { return results; }
    public void setResults(List<String> results) { this.results = results; }

}
