package lt.liutikas.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class CustomJsonConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

//        ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
//                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//                .simpleDateFormat(test.equalsIgnoreCase("A") ? "MM/dd/yyyy" : "MM-dd-yyyy")
//                .serializers(
//                        new LocalDateSerializer(test.equalsIgnoreCase("A") ? DateTimeFormatter.ofPattern("yyyy/MM/dd")
//                                : DateTimeFormatter.ofPattern("dd-MM-yyyy")),
//                        new LocalDateTimeSerializer(
//                                test.equalsIgnoreCase("A") ? DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
//                                        : DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
//                .deserializers(
//                        new LocalDateDeserializer(test.equalsIgnoreCase("A") ? DateTimeFormatter.ofPattern("yyyy/MM/dd")
//                                : DateTimeFormatter.ofPattern("dd-MM-yyyy")),
//                        new LocalDateTimeDeserializer(
//                                test.equalsIgnoreCase("A") ? DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
//                                        : DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
//                .build();
//
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
//        converters.add(converter);
    }
}