package lt.liutikas.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ReviewTypeConverter implements AttributeConverter<ReviewType, String> {

    @Override
    public String convertToDatabaseColumn(ReviewType vendorType) {
        if (vendorType == null) {
            return null;
        }
        return vendorType.getCode();
    }

    @Override
    public ReviewType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ReviewType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
