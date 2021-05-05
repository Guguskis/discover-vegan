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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductAssembler productAssembler;
    private final ProductRepository productRepository;
    private final SearchRequestRepository searchRequestRepository;
    private final VendorProductRepository vendorProductRepository;
    private final ProductVendorAssembler productVendorAssembler;


    public ProductService(ProductAssembler productAssembler, ProductRepository productRepository, SearchRequestRepository searchRequestRepository, VendorProductRepository vendorProductRepository, ProductVendorAssembler productVendorAssembler) {
        this.productAssembler = productAssembler;
        this.productRepository = productRepository;
        this.searchRequestRepository = searchRequestRepository;
        this.vendorProductRepository = vendorProductRepository;
        this.productVendorAssembler = productVendorAssembler;
    }

    public ProductsPageDto getProducts(Pageable pageable, String query) {
        String formattedQuery = "%" + query + "%";

        Page<Product> productsPage = productRepository.findByNameLikeIgnoreCaseOrderByNameAsc(pageable, formattedQuery);
        Pageable nextPageable = productsPage.nextPageable();

        ProductsPageDto productsPageDto = new ProductsPageDto();

        List<ProductDto> productDtos = productsPage.getContent()
                .stream()
                .filter(product -> {
                    List<VendorProduct> vendorProducts = product.getVendorProducts();
                    return vendorProducts != null && vendorProducts.size() > 0;
                })
                .map(productAssembler::assembleProduct)
                .collect(Collectors.toList());

        productsPageDto.setProducts(productDtos);
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

    public ProductVendorPage getVendorsAndProductDetails(PageRequest pageable, Integer productId) {

        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %d}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        Page<VendorProduct> vendorProductPage = vendorProductRepository.findAllByProduct(product.get(), pageable);
        Pageable nextPageable = vendorProductPage.nextPageable();

        List<VendorByProductDto> vendorByProductDtos = vendorProductPage.stream()
                .map(productVendorAssembler::assemble)
                .collect(Collectors.toList());

        ProductVendorPage productVendorPage = new ProductVendorPage();
        productVendorPage.setDetails(vendorByProductDtos);

        if (nextPageable.isPaged()) {
            productVendorPage.setNextPageToken(nextPageable.getPageNumber());
        }

        saveSearchRequest(product.get());

        return productVendorPage;
    }

    private void saveSearchRequest(Product product) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProduct(product);
        searchRequestRepository.save(searchRequest);
    }
}
