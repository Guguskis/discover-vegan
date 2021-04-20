package lt.liutikas.controller;

import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.entity.Product;
import lt.liutikas.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductsPageDto> getProducts(@Min(0) @RequestParam(value = "pageToken", defaultValue = "0") Integer pageToken,
                                                       @Min(1) @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageToken, pageSize);
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto));
    }
}
