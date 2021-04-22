package lt.liutikas.repository;

import lt.liutikas.dto.GetVendorDto;
import lt.liutikas.dto.PlaceDto;
import lt.liutikas.dto.PlacesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaceRepository {

    private static final List<String> FOOD_PLACE_TYPES = Arrays.asList("food", "restaurant", "store", "cafe", "bar", "supermarket");
    private static final String FOOD_PLACES_ENDPOINT = "/maps/api/place/nearbysearch/json?location={location}&rankby=distance&keyword={keyword}&pagetoken={next_page_token}";

    private final RestTemplate googleRestTemplate;

    public PlaceRepository(RestTemplate googleRestTemplate) {
        this.googleRestTemplate = googleRestTemplate;
    }

    public List<PlaceDto> getFoodPlaces(GetVendorDto getVendorDto) {

        String coordinates = String.format("%f,%f", getVendorDto.getLatitude(), getVendorDto.getLongitude());


        List<PlaceDto> places = new ArrayList<>();
        PlacesResponse placesResponse = new PlacesResponse();

        do {
            ResponseEntity<PlacesResponse> responseEntity = googleRestTemplate.getForEntity(
                    FOOD_PLACES_ENDPOINT,
                    PlacesResponse.class,
                    coordinates,
                    "food",
                    placesResponse.getNext_page_token());

            placesResponse = responseEntity.getBody();

            if (placesResponse.getResults() == null) {
                return Collections.emptyList();
            }

            places.addAll(placesResponse.getResults());
        } while (placesResponse.getNext_page_token() != null);

        return filterFoodPlaces(places);
    }

    private List<PlaceDto> filterFoodPlaces(List<PlaceDto> places) {
        return places.stream()
                .filter(placeDto -> placeDto.getTypes().stream()
                        .anyMatch(FOOD_PLACE_TYPES::contains))
                .collect(Collectors.toList());
    }
}
