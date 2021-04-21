package lt.liutikas.dto;

import java.util.List;

public class PlacesResponse {

    private String next_page_token;
    public List<PlaceDto> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public List<PlaceDto> getResults() {
        return results;
    }

    public void setResults(List<PlaceDto> results) {
        this.results = results;
    }
}
