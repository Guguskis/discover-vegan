package lt.liutikas.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Clock;

@Component
public class WebConfig {

    @Value("${secrets.googleApiKey}")
    private String googleApiKey;

    @Bean("place")
    public RestTemplate getGoogleRestTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("https://maps.googleapis.com")
                .build();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpRequest modifiedRequest = appendQueryParameter(request, "key", googleApiKey);
            return execution.execute(modifiedRequest, body);
        });
        return restTemplate;
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    private HttpRequest appendQueryParameter(HttpRequest request, String key, String value) {
        URI uri = UriComponentsBuilder
                .fromUri(request.getURI())
                .queryParam(key, value)
                .build()
                .toUri();
        return new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return uri;
            }
        };
    }
}
