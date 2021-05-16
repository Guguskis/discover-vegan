package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.assembler.ProductVendorAssembler;
import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductDto;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.repository.MongoProductRepository;
import lt.liutikas.repository.MongoSearchRequestRepository;
import lt.liutikas.repository.MongoVendorProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private MongoProductRepository mongoProductRepository;
    @Mock
    private MongoVendorProductRepository mongoVendorProductRepository;
    @Mock
    private MongoSearchRequestRepository mongoSearchRequestRepository;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(new ProductAssembler(),
                mongoProductRepository,
                mongoVendorProductRepository,
                mongoSearchRequestRepository,
                new ProductVendorAssembler(new VendorAssembler(), new VendorProductAssembler()));
    }

    @Test
    public void createProduct_givenAllData_createdProduct() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
            setProducer("Sun wheat");
        }};

        Product product = new Product() {{
            setProductId(1);
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

//        when(productRepository.save(any(Product.class)))
//                .thenReturn(product);

        ProductDto createdProduct = productService.createProduct(createProductDto);

//        verify(productRepository, times(1))
//                .save(any(Product.class));

        assertEquals(product.getProductId(), createdProduct.getProductId());
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getProducer(), createdProduct.getProducer());
        assertEquals(product.getImageUrl(), createdProduct.getImageUrl());
    }

    @Test(expected = BadRequestException.class)
    public void createProduct_providedInvalidDataModel_throwsBadRequestException() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
        }};

//        when(productRepository.save(any(Product.class)))
//                .thenThrow(DataIntegrityViolationException.class);

        productService.createProduct(createProductDto);
    }

    @Test
    public void getProducts_queriedOneProduct_returnsFullyMappedProduct() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Product product = new Product() {{
            setProductId(1);
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

        List<Product> products = Collections.singletonList(product);
//        when(productRepository.findByNameLikeIgnoreCaseOrderByNameAsc(any(PageRequest.class), any()))
//                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");
        assertEquals(1, productsPageDto.getProducts().size());
        ProductDto returnedProduct = productsPageDto.getProducts().get(0);

        assertEquals(product.getProductId(), returnedProduct.getProductId());
        assertEquals(product.getName(), returnedProduct.getName());
        assertEquals(product.getProducer(), returnedProduct.getProducer());
        assertEquals(product.getImageUrl(), returnedProduct.getImageUrl());
    }

    @Test
    public void getProducts_pageSizeSmallerThanProductCount_returnedNextPageToken() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Product> products = Arrays.asList(
                new Product(),
                new Product(),
                new Product()
        );

//        when(productRepository.findAll(any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(products, pageRequest, 3));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");

//        verify(productRepository, times(1))
//                .findAll(any(PageRequest.class));
        assertEquals(3, productsPageDto.getProducts().size());
        assertEquals(Integer.valueOf(1), productsPageDto.getNextPageToken());
    }

    @Test
    public void getProducts_pageSizeBiggerThanProductCount_nextPageTokenIsNull() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(
                new Product(),
                new Product()
        );

//        when(productRepository.findAll(any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(products, pageRequest, 2));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");

//        verify(productRepository, times(1))
//                .findAll(any(PageRequest.class));
        assertEquals(2, productsPageDto.getProducts().size());
        assertNull(productsPageDto.getNextPageToken());
    }
}