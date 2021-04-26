package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.model.Product;
import lt.liutikas.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductAssembler productAssembler;
    private final ProductRepository productRepository;

    public ProductService(ProductAssembler productAssembler, ProductRepository productRepository) {
        this.productAssembler = productAssembler;
        this.productRepository = productRepository;
    }

    public ProductsPageDto getProducts(Pageable pageable, String query) {
        String formattedQuery = "%" + query + "%";

        Page<Product> productsPage = productRepository.findByNameLikeIgnoreCaseOrderByNameAsc(pageable, formattedQuery);
        Pageable nextPageable = productsPage.nextPageable();

        ProductsPageDto productsPageDto = new ProductsPageDto();
        productsPageDto.setProducts(productsPage.getContent());
        if (nextPageable.isPaged()) {
            productsPageDto.setNextPageToken(nextPageable.getPageNumber());
        }

        LOG.info(String.format("Returned products {query: '%s', pageToken: %d, pageSize: %d}", query, pageable.getPageNumber(), pageable.getPageSize()));

        return productsPageDto;
    }

    public Product createProduct(CreateProductDto createProductDto) {
        Product product = productAssembler.assembleProduct(createProductDto);

        try {
            product = productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            String message = "Failed to insert product in database";
            LOG.error(message, exception);
            throw new BadRequestException(message);
        }

        LOG.info(String.format("Created new product {productId: %d, name: '%s', producer: '%s'}",
                product.getProductId(),
                product.getName(),
                product.getProducer()));

        return product;
    }
}
