package lt.liutikas.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class VendorTypeConverter implements AttributeConverter<VendorType, String> {

    @Override
    public String convertToDatabaseColumn(VendorType vendorType) {
        if (vendorType == null) {
            return null;
        }
        return vendorType.getCode();
    }

    @Override
    public VendorType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(VendorType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
