package lt.liutikas.assembler;

import lt.liutikas.dto.CreateVendorDto;
import lt.liutikas.dto.Location;
import lt.liutikas.dto.PlaceDto;
import lt.liutikas.entity.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorAssembler {

    public Vendor assembleVendor(CreateVendorDto createVendorDto) {
        Vendor vendor = new Vendor();

        vendor.setName(createVendorDto.getName());
        vendor.setImageUrl(createVendorDto.getImageUrl());
        vendor.setLatitude(createVendorDto.getLatitude());
        vendor.setLatitude(createVendorDto.getLongitude());

        return vendor;
    }

    public Vendor assembleVendor(PlaceDto placeDto) {
        Location location = placeDto.getGeometry().getLocation();

        Vendor vendor = new Vendor();

        vendor.setName(placeDto.getName());
        vendor.setExternalPlaceId(placeDto.getPlace_id());
        vendor.setLatitude(location.getLat());
        vendor.setLongitude(location.getLng());
        vendor.setAddress(placeDto.getVicinity());

        return vendor;
    }

}
