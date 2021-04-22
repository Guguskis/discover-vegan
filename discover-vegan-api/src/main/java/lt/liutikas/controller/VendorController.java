package lt.liutikas.controller;

import lt.liutikas.dto.*;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorType;
import lt.liutikas.service.VendorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getVendors(@RequestParam Double latitude,
                                                   @RequestParam Double longitude,
                                                   @RequestParam(required = false) VendorType type) {// todo add VendorType {PHYSICAL, DIGITAL}, return all if not provided
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        GetVendorDto getVendorDto = new GetVendorDto();
        getVendorDto.setLocation(location);
        getVendorDto.setType(type);

        return ResponseEntity.ok(vendorService.getVendors(getVendorDto));
    }

    @PostMapping // todo for development, remove later
    public ResponseEntity<Vendor> createVendor(@Valid CreateVendorDto createVendorDto) {
        return ResponseEntity.ok(vendorService.createVendor(createVendorDto));
    }

    @GetMapping("/{vendorId}/product")
    public ResponseEntity<VendorProductPageDto> getProducts(@PathVariable Integer vendorId,
                                                            @Min(0) @RequestParam(value = "pageToken", defaultValue = "0") Integer pageToken,
                                                            @Min(1) @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageToken, pageSize);
        return ResponseEntity.ok(vendorService.getProducts(vendorId, pageRequest));
    }

    @PostMapping("/{vendorId}/product")
    public ResponseEntity<VendorProductDto> createProduct(@PathVariable Integer vendorId,
                                                          @RequestBody @Valid CreateVendorProductDto createVendorProductDto) {
        return ResponseEntity.ok(vendorService.createProduct(vendorId, createVendorProductDto));
    }
}
