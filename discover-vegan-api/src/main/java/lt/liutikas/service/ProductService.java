package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductAssembler productAssembler;
    private final ProductRepository productRepository;

    public ProductService(ProductAssembler productAssembler, ProductRepository productRepository) {
        this.productAssembler = productAssembler;
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        LOG.info(String.format("Returned products {name: \"%s\"}", "Todo name or query or whatever"));
        return productRepository.findAll();
    }

    public Product createProduct(CreateProductDto createProductDto) {
        Product product = productAssembler.assembleProduct(createProductDto);
        product = productRepository.save(product);
        return product;
    }
}
