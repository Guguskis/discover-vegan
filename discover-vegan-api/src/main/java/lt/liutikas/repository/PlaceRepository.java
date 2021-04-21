package lt.liutikas.repository;

import lt.liutikas.dto.GetVendorDto;
import lt.liutikas.dto.PlaceDto;
import lt.liutikas.dto.PlacesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class PlaceRepository {

    private static final String FOOD_PLACES_URL = "/maps/api/place/nearbysearch/json?location={location}&radius={radius}&type=supermarket";
    private static final int SEARCH_RADIUS = 5000;

    private final RestTemplate googleRestTemplate;

    public PlaceRepository(RestTemplate googleRestTemplate) {
        this.googleRestTemplate = googleRestTemplate;
    }

    public List<PlaceDto> getFoodPlaces(GetVendorDto getVendorDto) {

        String coordinates = String.format("%f,%f", getVendorDto.getLatitude(), getVendorDto.getLongitude());

        ResponseEntity<PlacesResponse> responseEntity = googleRestTemplate.getForEntity(
                FOOD_PLACES_URL,
                PlacesResponse.class,
                coordinates,
                String.valueOf(SEARCH_RADIUS));

        PlacesResponse placesResponse = responseEntity.getBody();

        if (placesResponse.getResults() == null) {
            return Collections.emptyList();
        }

        return placesResponse.getResults();
    }
}
