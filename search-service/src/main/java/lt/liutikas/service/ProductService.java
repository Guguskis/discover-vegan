package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.assembler.ProductVendorAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.model.Product;
import lt.liutikas.model.SearchRequest;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.SearchRequestRepository;
import lt.liutikas.repository.VendorProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductAssembler productAssembler;
    private final ProductRepository productRepository;
    private final VendorProductRepository vendorProductRepository;
    private final SearchRequestRepository searchRequestRepository;
    private final ProductVendorAssembler productVendorAssembler;


    public ProductService(ProductAssembler productAssembler, ProductRepository productRepository, VendorProductRepository vendorProductRepository, SearchRequestRepository searchRequestRepository, ProductVendorAssembler productVendorAssembler) {
        this.productAssembler = productAssembler;
        this.productRepository = productRepository;
        this.vendorProductRepository = vendorProductRepository;
        this.searchRequestRepository = searchRequestRepository;
        this.productVendorAssembler = productVendorAssembler;
    }

    public ProductsPageDto getProducts(Pageable pageable, String query) {
        Page<Product> productsPage = productRepository.findByNameRegexOrderByNameAsc(pageable, query);
        Pageable nextPageable = productsPage.nextPageable();

        ProductsPageDto productsPageDto = new ProductsPageDto();

        List<ProductDto> productDtos = productsPage.getContent()
                .stream()// todo filter with at least 1 VendorProduct
                .map(productAssembler::assembleMongoProduct)
                .collect(Collectors.toList());

        productsPageDto.setProducts(productDtos);
        if (nextPageable.isPaged()) {
            productsPageDto.setNextPageToken(nextPageable.getPageNumber());
        }

        LOG.info(String.format("Returned products {query: '%s', pageToken: %d, pageSize: %d}", query, pageable.getPageNumber(), pageable.getPageSize()));

        return productsPageDto;
    }

    public ProductDto createProduct(CreateProductDto createProductDto) {
        String nameWithAccents = createProductDto.getName();
        String nameWithoutAccents = StringUtils.stripAccents(nameWithAccents);
        createProductDto.setName(nameWithoutAccents);

        Product product = productAssembler.assembleMongoProduct(createProductDto);

        try {
            product = productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            String message = "Failed to insert product in database";
            LOG.error(message, exception);
            throw new BadRequestException(message);
        }

        LOG.info(String.format("Created new product {productId: %s, name: '%s', producer: '%s'}",
                product.getId(),
                product.getName(),
                product.getProducer()));

        return productAssembler.assembleMongoProduct(product);
    }

    public ProductVendorPage getVendorsAndProductDetails(PageRequest pageable, String productId) {

        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %s}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        Page<VendorProduct> vendorProductPage = vendorProductRepository.findAllByProduct(product.get(), pageable); // todo check if this works
        Pageable nextPageable = vendorProductPage.nextPageable();

        List<VendorByProductDto> vendorByProductDtos = vendorProductPage.stream()
                .map(productVendorAssembler::assemble)
                .collect(Collectors.toList());

        ProductVendorPage productVendorPage = new ProductVendorPage();
        productVendorPage.setDetails(vendorByProductDtos);

        if (nextPageable.isPaged()) {
            productVendorPage.setNextPageToken(nextPageable.getPageNumber());
        }

        LOG.info(String.format("Returned vendors where product can be bought {productId: %s}", productId));

        saveSearchRequest(product.get());

        return productVendorPage;
    }

    private void saveSearchRequest(Product product) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProduct(product);
        searchRequest.setCreatedAt(LocalDateTime.now());
        searchRequestRepository.save(searchRequest);
    }
}
