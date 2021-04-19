package lt.liutikas.controller;

import lt.liutikas.dto.CreateVendorDto;
import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import lt.liutikas.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getVendors() {
        return ResponseEntity.ok(vendorService.getVendors());
    }

    @PostMapping // todo for development, remove later
    public ResponseEntity<Vendor> createVendor(@Valid CreateVendorDto createVendorDto) {
        return ResponseEntity.ok(vendorService.createVendor(createVendorDto));
    }

    @GetMapping("/{vendorId}/product")
    public ResponseEntity<List<VendorProduct>> getProducts(@PathVariable Integer vendorId) {
        return ResponseEntity.ok(vendorService.getProducts(vendorId));
    }

    @PostMapping("/{vendorId}/product")
    public ResponseEntity<VendorProductDto> createProduct(@PathVariable Integer vendorId,
                                                          @RequestBody @Valid CreateVendorProductDto createVendorProductDto) {
        return ResponseEntity.ok(vendorService.createProduct(vendorId, createVendorProductDto));
    }
}
