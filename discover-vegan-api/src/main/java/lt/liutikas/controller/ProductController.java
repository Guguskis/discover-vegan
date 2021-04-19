package lt.liutikas.controller;

import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService vendorService) {
        this.productService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam("name") String name) {
        return ResponseEntity.ok(productService.getProducts(name));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto));
    }
}
