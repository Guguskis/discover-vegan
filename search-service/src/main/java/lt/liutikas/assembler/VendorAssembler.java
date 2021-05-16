package lt.liutikas.assembler;

import lt.liutikas.dto.Location;
import lt.liutikas.dto.Place;
import lt.liutikas.dto.VendorDto;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorType;
import org.springframework.stereotype.Component;

@Component
public class VendorAssembler {

    public Vendor assembleVendor(Place place) {
        Location location = place.getGeometry().getLocation();

        Vendor vendor = new Vendor();

        vendor.setName(place.getName());
        vendor.setExternalPlaceId(place.getPlace_id());
        vendor.setLatitude(location.getLat());
        vendor.setLongitude(location.getLng());
        vendor.setAddress(place.getVicinity());
        vendor.setVendorType(VendorType.valueOf(place.getVendorType().toString()));

        return vendor;
    }

    public VendorDto assembleVendor(Vendor vendor) {
        VendorDto vendorDto = new VendorDto();

        vendorDto.setVendorId(vendor.getId());
        vendorDto.setName(vendor.getName());
        vendorDto.setLatitude(vendor.getLatitude());
        vendorDto.setLongitude(vendor.getLongitude());
        vendorDto.setAddress(vendor.getAddress());
        vendorDto.setVendorType(vendor.getVendorType());

        return vendorDto;
    }
}
