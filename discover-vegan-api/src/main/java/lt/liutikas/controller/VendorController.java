package lt.liutikas.controller;

import lt.liutikas.dto.CreateVendorDto;
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

    @GetMapping("/{id}/product")
    public ResponseEntity<List<VendorProduct>> getProducts(@PathVariable Integer id) {
        return ResponseEntity.ok(vendorService.getProducts(id));
    }
}
