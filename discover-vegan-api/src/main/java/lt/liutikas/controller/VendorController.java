package lt.liutikas.controller;

import lt.liutikas.entity.Product;
import lt.liutikas.entity.Vendor;
import lt.liutikas.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}/product")
    public ResponseEntity<List<Product>> getProducts(@PathVariable Integer id) {
        return ResponseEntity.ok(vendorService.getProducts(id));
    }
}
