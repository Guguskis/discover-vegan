package lt.liutikas.assembler;

import lt.liutikas.dto.CreateVendorDto;
import lt.liutikas.entity.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorAssembler {

    public Vendor assemblerVendor(CreateVendorDto createVendorDto) {
        Vendor vendor = new Vendor();

        vendor.setName(createVendorDto.getName());
        vendor.setImageUrl(createVendorDto.getImageUrl());
        vendor.setLatitude(createVendorDto.getLatitude());
        vendor.setLatitude(createVendorDto.getLongitude());

        return vendor;
    }

}
