package lt.liutikas.repository;

import lt.liutikas.dto.Location;
import lt.liutikas.dto.Place;
import lt.liutikas.dto.PlacesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaceRepository {

    private static final String PLACES_ENDPOINT = "/maps/api/place/nearbysearch/json?location={location}&rankby=distance&keyword={keyword}&pagetoken={next_page_token}";

    private final RestTemplate googleRestTemplate;

    public PlaceRepository(RestTemplate googleRestTemplate) {
        this.googleRestTemplate = googleRestTemplate;
    }

    public List<Place> getPlaces(Location location, Place.Type keyword) {
        List<Place> places = new ArrayList<>();
        PlacesResponse placesResponse = new PlacesResponse();

        do {
            ResponseEntity<PlacesResponse> responseEntity = googleRestTemplate.getForEntity(
                    PLACES_ENDPOINT,
                    PlacesResponse.class,
                    String.format("%f,%f", location.getLat(), location.getLng()),
                    keyword,
                    placesResponse.getNext_page_token());

            placesResponse = responseEntity.getBody();
            places.addAll(placesResponse.getResults());

        } while (placesResponse.getNext_page_token() != null);

        return places;
    }
}
