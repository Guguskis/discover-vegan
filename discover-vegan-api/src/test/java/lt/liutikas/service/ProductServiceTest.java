package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.entity.Product;
import lt.liutikas.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(new ProductAssembler(), productRepository);
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

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Product createdProduct = productService.createProduct(createProductDto);

        verify(productRepository, times(1))
                .save(any(Product.class));

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

        when(productRepository.save(any(Product.class)))
                .thenThrow(DataIntegrityViolationException.class);

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
        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest);
        assertEquals(1, productsPageDto.getProducts().size());
        Product returnedProduct = productsPageDto.getProducts().get(0);

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

        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(products, pageRequest, 3));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest);

        verify(productRepository, times(1))
                .findAll(any(PageRequest.class));
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

        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(products, pageRequest, 2));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest);

        verify(productRepository, times(1))
                .findAll(any(PageRequest.class));
        assertEquals(2, productsPageDto.getProducts().size());
        assertNull(productsPageDto.getNextPageToken());
    }
}