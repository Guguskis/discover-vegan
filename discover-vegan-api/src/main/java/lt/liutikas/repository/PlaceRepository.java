package lt.liutikas.repository;

import lt.liutikas.dto.PlaceDto;
import lt.liutikas.dto.PlacesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class PlaceRepository {

    private static final String FOOD_PLACES_URL = "/maps/api/place/nearbysearch/json?location={location}&radius={radius}&type=food";
    private final RestTemplate googleRestTemplate;

    public PlaceRepository(RestTemplate googleRestTemplate) {
        this.googleRestTemplate = googleRestTemplate;
    }

    public List<PlaceDto> getFoodPlaces() {
        ResponseEntity<PlacesResponse> responseEntity = googleRestTemplate.getForEntity(
                FOOD_PLACES_URL,
                PlacesResponse.class,
                "54.72744555070343,25.341746138622348",
                "5000");

        PlacesResponse placesResponse = responseEntity.getBody();
        if (placesResponse.getResults() != null) {
            return placesResponse.getResults();
        }

        return Collections.emptyList();
    }
}
