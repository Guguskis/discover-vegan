package lt.liutikas.controller;

import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductVendorPage;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.model.Product;
import lt.liutikas.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/product")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService vendorService) {
        this.productService = vendorService;
    }

    @GetMapping
    public ResponseEntity<ProductsPageDto> getProducts(@RequestParam(defaultValue = "0") @Min(0) Integer pageToken,
                                                       @RequestParam(defaultValue = "50") @Min(1) Integer pageSize,
                                                       @RequestParam String query) {
        PageRequest pageable = PageRequest.of(pageToken, pageSize);
        return ResponseEntity.ok(productService.getProducts(pageable, query));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto));
    }

    @GetMapping("/{productId}/vendor")
    public ResponseEntity<ProductVendorPage> getVendorsAndProductDetails(@RequestParam(defaultValue = "0") @Min(0) Integer pageToken,
                                                                         @RequestParam(defaultValue = "50") @Min(1) Integer pageSize,
                                                                         @PathVariable Integer productId) {
        PageRequest pageable = PageRequest.of(pageToken, pageSize);
        return ResponseEntity.ok(productService.getVendorsAndProductDetails(pageable, productId));
    }
}
